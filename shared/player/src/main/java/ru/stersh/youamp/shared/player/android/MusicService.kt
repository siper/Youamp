package ru.stersh.youamp.shared.player.android

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionError
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.guava.asListenableFuture
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import ru.stersh.youamp.shared.player.library.MediaLibraryRepository
import ru.stersh.youamp.shared.player.utils.PlayerThread
import ru.stersh.youamp.shared.player.utils.mediaItems
import timber.log.Timber

class MusicService : MediaLibraryService() {

    private val mediaLibraryRepository: MediaLibraryRepository by inject()
    private val apiSonicPlayQueueSyncer: ApiSonicPlayQueueSyncer by inject()

    private lateinit var player: Player
    private lateinit var mediaSession: MediaLibrarySession

    private val playerListener = object : Player.Listener {
        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            Handler(Looper.getMainLooper()).post {
                onUpdateNotification(mediaSession, true)
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Timber.w(error)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        val customCallback = CustomMediaSessionCallback()

        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        val okHttpDataSource = OkHttpDataSource
            .Factory(okHttpClient)
            .setDefaultRequestProperties(emptyMap())

        val resolver = ResolvingDataSource.Resolver {
            it
                .buildUpon()
                .setHttpRequestHeaders(emptyMap())
                .build()
        }

        val resolvingDataSource = ResolvingDataSource.Factory(okHttpDataSource, resolver)

        player = ExoPlayer
            .Builder(this)
            .setLooper(PlayerThread.looper)
            .setMediaSourceFactory(DefaultMediaSourceFactory(resolvingDataSource))
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .build()

        mediaSession = MediaLibrarySession
            .Builder(this, player, customCallback)
            .build()

        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)

        mediaSession.setSessionActivity(pendingIntent)

        addSession(mediaSession)
        player.addListener(playerListener)
    }

    override fun onDestroy() {
        mediaSession.release()
        player.release()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    private inner class CustomMediaSessionCallback : MediaLibrarySession.Callback {

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>,
        ): ListenableFuture<MutableList<MediaItem>> {
            val updatedMediaItems = mediaItems.map { mediaItem ->
                mediaItem
                    .buildUpon()
                    .setUri(mediaItem.requestMetadata.mediaUri)
                    .build()
            }.toMutableList()
            return Futures.immediateFuture(updatedMediaItems)
        }

        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<MediaItem>> {
            val root = MediaItem.Builder()
                .setMediaId(LIBRARY_ROOT_ID)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle("Root")
                        .setIsPlayable(false)
                        .setIsBrowsable(true)
                        .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
                        .build()
                )
                .build()
                .let { LibraryResult.ofItem(it, params) }
            return Futures.immediateFuture(root)
        }

        @OptIn(UnstableApi::class)
        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            return when {
                parentId == LIBRARY_ROOT_ID -> {
                    val items = listOf(
                        MediaItem.Builder()
                            .setMediaId(LIBRARY_PLAYLISTS_ID)
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setTitle("Playlists")
                                    .setIsPlayable(false)
                                    .setIsBrowsable(true)
                                    .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_PLAYLISTS)
                                    .build()
                            )
                            .build()
                    )
                    Futures.immediateFuture(LibraryResult.ofItemList(items, params))
                }

                parentId == LIBRARY_PLAYLISTS_ID -> {
                    runBlocking {
                        async {
                            mediaLibraryRepository
                                .getPlaylists()
                                .map {
                                    MediaItem.Builder()
                                        .setMediaId(LIBRARY_PLAYLIST_PREFIX + it.id)
                                        .setMediaMetadata(
                                            MediaMetadata.Builder()
                                                .setTitle(it.title)
                                                .setArtworkUri(it.coverUrl?.toUri())
                                                .setIsPlayable(true)
                                                .setIsBrowsable(false)
                                                .setMediaType(MediaMetadata.MEDIA_TYPE_PLAYLIST)
                                                .build()
                                        )
                                        .build()
                                }
                                .let {
                                    LibraryResult.ofItemList(it, params)
                                }
                        }.asListenableFuture()
                    }
                }

                parentId.startsWith(LIBRARY_PLAYLIST_PREFIX) -> {
                    runBlocking {
                        async {
                            mediaLibraryRepository
                                .getPlaylistSongs(parentId.replace(LIBRARY_PLAYLIST_PREFIX, ""))
                                .map {
                                    MediaItem
                                        .Builder()
                                        .setMediaId(it.id)
                                        .setMediaMetadata(
                                            MediaMetadata
                                                .Builder()
                                                .setTitle(it.title)
                                                .setArtworkUri(it.coverUrl?.toUri())
                                                .setIsPlayable(true)
                                                .setIsBrowsable(false)
                                                .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                                                .build()
                                        )
                                        .build()
                                }
                                .let {
                                    LibraryResult.ofItemList(it, params)
                                }
                        }
                            .asListenableFuture()
                    }
                }

                else -> Futures.immediateFuture(LibraryResult.ofError(SessionError.ERROR_BAD_VALUE))
            }
        }

        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String
        ): ListenableFuture<LibraryResult<MediaItem>> {
            return super.onGetItem(session, browser, mediaId)
        }

        @OptIn(UnstableApi::class)
        override fun onSetMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>,
            startIndex: Int,
            startPositionMs: Long
        ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
            val item = mediaItems.firstOrNull() ?: return super.onSetMediaItems(
                mediaSession,
                controller,
                mediaItems,
                startIndex,
                startPositionMs
            )
            if (item.mediaId.startsWith(LIBRARY_PLAYLIST_PREFIX)) {
                return runBlocking {
                    async {
                        mediaLibraryRepository
                            .getPlaylistSongs(item.clearId)
                            .map {
                                MediaItem
                                    .Builder()
                                    .setMediaId(it.id)
                                    .setMediaMetadata(
                                        MediaMetadata
                                            .Builder()
                                            .setTitle(it.title)
                                            .setArtworkUri(it.coverUrl?.toUri())
                                            .setIsPlayable(true)
                                            .setIsBrowsable(false)
                                            .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                                            .build()
                                    )
                                    .build()
                            }
                            .let {
                                MediaSession.MediaItemsWithStartPosition(it, 0, 0L)
                            }
                    }
                        .asListenableFuture()
                }
            }
            return super.onSetMediaItems(mediaSession, controller, mediaItems, startIndex, startPositionMs)
        }

        override fun onGetSearchResult(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            query: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?,
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
            val search = player.mediaItems.filter {
                it.mediaMetadata.title?.contains(query, true) == true ||
                        it.mediaMetadata.artist?.contains(query, true) == true ||
                        it.mediaMetadata.albumTitle?.contains(query, true) == true ||
                        it.mediaMetadata.albumArtist?.contains(query, true) == true
            }
            return Futures.immediateFuture(LibraryResult.ofItemList(search, params))
        }

        val MediaItem.clearId: String
            get() = mediaId.replace(LIBRARY_PLAYLIST_PREFIX, "")
    }

    companion object {
        private const val LIBRARY_ROOT_ID = "[ROOT_ID]"
        private const val LIBRARY_PLAYLISTS_ID = "[PLAYLISTS_ID]"
        private const val LIBRARY_PLAYLIST_PREFIX = "playlist_"
    }
}
