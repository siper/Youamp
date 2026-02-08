package ru.stersh.youamp.audio

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
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
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import ru.stersh.youamp.MainActivity
import ru.stersh.youamp.audio.auto.AutoMediaSessionCallback
import ru.stersh.youamp.audio.auto.AutoRepository
import co.touchlab.kermit.Logger as Log

class MusicService : MediaLibraryService() {
    private val autoRepository: AutoRepository by inject()

    private lateinit var player: Player
    private lateinit var mediaSession: MediaLibrarySession

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val playerListener =
        object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                Handler(Looper.getMainLooper()).post {
                    onUpdateNotification(
                        mediaSession,
                        true,
                    )
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e(error) { "Player error" }
            }
        }

    @OptIn(UnstableApi::class)
    private fun createPlayer(): Player {
        val okHttpClient =
            OkHttpClient
                .Builder()
                .build()

        val okHttpDataSource =
            OkHttpDataSource
                .Factory(okHttpClient)
                .setDefaultRequestProperties(emptyMap())

        val resolver =
            ResolvingDataSource.Resolver {
                it
                    .buildUpon()
                    .setHttpRequestHeaders(emptyMap())
                    .build()
            }

        val resolvingDataSource =
            ResolvingDataSource.Factory(
                okHttpDataSource,
                resolver,
            )

        return ExoPlayer
            .Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(resolvingDataSource))
            .setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true,
            ).setWakeMode(C.WAKE_MODE_NETWORK)
            .build()
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        player = createPlayer()

        val autoCallback =
            AutoMediaSessionCallback(
                scope = scope,
                autoRepository = autoRepository,
            )

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT,
            )

        mediaSession =
            MediaLibrarySession
                .Builder(
                    this,
                    player,
                    autoCallback,
                ).setSessionActivity(pendingIntent)
                .build()

        setShowNotificationForIdlePlayer(SHOW_NOTIFICATION_FOR_IDLE_PLAYER_ALWAYS)

        setMediaNotificationProvider(
            DefaultMediaNotificationProvider(this),
        )

        player.addListener(playerListener)
    }

    override fun onDestroy() {
        mediaSession.release()
        player.release()
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession
}
