package ru.stersh.youamp.shared.player.android

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import ru.stersh.youamp.shared.player.library.MediaLibraryRepository
import ru.stersh.youamp.shared.player.utils.PlayerThread
import timber.log.Timber

class MusicService : MediaLibraryService() {

    private val mediaLibraryRepository: MediaLibraryRepository by inject()

    private lateinit var player: Player
    private lateinit var mediaSession: MediaLibrarySession

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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

        val autoCallback = AutoMediaSessionCallback(
            context = this,
            scope = scope,
            mediaLibraryRepository = mediaLibraryRepository
        )

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
            .Builder(this, player, autoCallback)
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
}
