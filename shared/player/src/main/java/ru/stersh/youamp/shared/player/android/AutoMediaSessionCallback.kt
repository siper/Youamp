package ru.stersh.youamp.shared.player.android

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService.LibraryParams
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionError
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.future
import ru.stersh.youamp.player.R
import ru.stersh.youamp.shared.player.android.MediaLibrary.LIBRARY_PLAYLISTS_ID
import ru.stersh.youamp.shared.player.android.MediaLibrary.LIBRARY_ROOT_ID
import ru.stersh.youamp.shared.player.library.MediaLibraryRepository
import ru.stersh.youamp.shared.player.utils.mediaItems

internal class AutoMediaSessionCallback(
    private val context: Context,
    private val scope: CoroutineScope,
    private val mediaLibraryRepository: MediaLibraryRepository
) : MediaLibrarySession.Callback {

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
                                .setTitle(context.getString(R.string.playlists_title))
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
                scope.future {
                    mediaLibraryRepository
                        .getPlaylists()
                        .map { it.toMediaItem() }
                        .let {
                            LibraryResult.ofItemList(it, params)
                        }
                }
            }

            MediaLibrary.isPlaylist(parentId) -> {
                scope.future {
                    mediaLibraryRepository
                        .getPlaylistSongs(MediaLibrary.clearPlaylistId(parentId))
                        .map { it.toMediaItem() }
                        .let {
                            LibraryResult.ofItemList(it, params)
                        }
                }
            }

            else -> Futures.immediateFuture(LibraryResult.ofError(SessionError.ERROR_BAD_VALUE))
        }
    }

    @OptIn(UnstableApi::class)
    override fun onSetMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        val item = mediaItems.firstOrNull { MediaLibrary.isPlaylist(it.mediaId) }
        if (item != null) {
            return scope.future {
                mediaLibraryRepository
                    .getPlaylistSongs(MediaLibrary.clearPlaylistId(item.mediaId))
                    .map { it.toMediaItem() }
                    .let {
                        MediaSession.MediaItemsWithStartPosition(it, 0, 0L)
                    }
            }
        }
        return super.onSetMediaItems(
            mediaSession,
            controller,
            mediaItems,
            startIndex,
            startPositionMs
        )
    }

    override fun onGetSearchResult(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        page: Int,
        pageSize: Int,
        params: LibraryParams?,
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        val search = session.player.mediaItems.filter {
            it.mediaMetadata.title?.contains(query, true) == true ||
                    it.mediaMetadata.artist?.contains(query, true) == true ||
                    it.mediaMetadata.albumTitle?.contains(query, true) == true ||
                    it.mediaMetadata.albumArtist?.contains(query, true) == true
        }
        return Futures.immediateFuture(LibraryResult.ofItemList(search, params))
    }
}