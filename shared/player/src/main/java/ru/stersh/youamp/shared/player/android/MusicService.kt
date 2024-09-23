package ru.stersh.youamp.shared.player.android

import androidx.annotation.OptIn
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
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import timber.log.Timber

class MusicService : MediaLibraryService() {

    private val apiSonicPlayQueueSyncer: ApiSonicPlayQueueSyncer by inject()

    private lateinit var player: Player
    private lateinit var mediaSession: MediaLibrarySession

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val playerListener = object : Player.Listener {
        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            onUpdateNotification(mediaSession, true)
            syncQueue()
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
            .setMediaSourceFactory(DefaultMediaSourceFactory(resolvingDataSource))
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .build()

        mediaSession = MediaLibrarySession
            .Builder(this, player, customCallback)
            .build()

        addSession(mediaSession)
        player.addListener(playerListener)

        loadPlayQueue()
        syncQueueLoop()
    }


    override fun onDestroy() {
        mediaSession.release()
        player.release()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    private fun loadPlayQueue() = serviceScope.launch {
        apiSonicPlayQueueSyncer.loadPlayQueue(player)
    }

    private fun syncQueue() = serviceScope.launch {
        apiSonicPlayQueueSyncer.syncPlayQueue(player)
    }

    private fun syncQueueLoop() = serviceScope.launch {
        withContext(Dispatchers.IO) {
            while (true) {
                delay(SYNC_QUEUE_PERIOD)
                apiSonicPlayQueueSyncer.syncPlayQueue(player)
            }
        }
    }

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

        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
        ): MediaSession.ConnectionResult {
            val connectionResult = super.onConnect(session, controller)
            return MediaSession.ConnectionResult.accept(
                connectionResult.availableSessionCommands,
                connectionResult.availablePlayerCommands,
            )
        }
    }

    companion object {
        private const val SYNC_QUEUE_PERIOD = 5000L
    }
}
