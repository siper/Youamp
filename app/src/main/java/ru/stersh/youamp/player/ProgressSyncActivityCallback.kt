package ru.stersh.youamp.player

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.utils.EmptyActivityLifecycleCallback
import ru.stersh.youamp.main.ui.MainActivity
import ru.stersh.youamp.shared.player.progress.PlayerProgressStore
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher

internal class ProgressSyncActivityCallback(
    private val playerProgressStore: PlayerProgressStore,
    private val playerProvider: PlayerProvider,
    private val apiProvider: ApiProvider
) : EmptyActivityLifecycleCallback() {
    private var scrobbleSender: ScrobbleSender? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is MainActivity) {
            return
        }
        playerProgressStore
            .playerProgress()
            .filterNotNull()
            .onEach { progress ->
                val player = playerProvider.get()
                val currentItemId = withContext(PlayerDispatcher) { player.currentMediaItem?.mediaId } ?: return@onEach
                if (currentItemId != scrobbleSender?.id) {
                    scrobbleSender = ScrobbleSender(currentItemId, apiProvider)
                }
                val sender = scrobbleSender ?: return@onEach
                if (progress.currentTimeMs > SEND_SCROBBLE_EVENT_TIME && !sender.scrobbleSent) {
                    activity.lifecycleScope.launch {
                        sender.trySendScrobble()
                    }
                }
                if ((progress.currentTimeMs + SEND_SCROBBLE_EVENT_TIME) >= progress.totalTimeMs && !sender.submissionSent) {
                    activity.lifecycleScope.launch {
                        sender.trySendSubmission()
                    }
                }
            }
            .launchIn(activity.lifecycleScope)
    }

    companion object {
        private const val SEND_SCROBBLE_EVENT_TIME = 5000L
    }
}