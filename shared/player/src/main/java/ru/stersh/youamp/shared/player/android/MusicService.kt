package ru.stersh.youamp.shared.player.android

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.os.Handler
import android.os.Looper
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
import okhttp3.OkHttpClient
import ru.stersh.youamp.shared.player.utils.PlayerThread
import timber.log.Timber

class MusicService : MediaLibraryService() {

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
}
