package ru.stersh.youamp.audio

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import ru.stersh.youamp.audio.auto.AutoMediaSessionCallback
import ru.stersh.youamp.audio.auto.AutoRepository
import timber.log.Timber

class MusicService : MediaLibraryService() {

    private val autoRepository: AutoRepository by inject()

    private val player: Player by inject()
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
            autoRepository = autoRepository
        )

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
