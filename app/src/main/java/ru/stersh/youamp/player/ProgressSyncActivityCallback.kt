package ru.stersh.youamp.player

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.utils.EmptyActivityLifecycleCallback
import ru.stersh.youamp.main.ui.MainActivity
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.core.player.Player

internal class ProgressSyncActivityCallback(
    private val player: Player,
    private val apiProvider: ApiProvider
) : EmptyActivityLifecycleCallback() {
    private var scrobbleSender: ScrobbleSender? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is MainActivity) {
            return
        }
        player
            .getProgress()
            .filterNotNull()
            .onEach { progress ->
                val currentItemId = player
                    .getCurrentMediaItem()
                    .first()?.id
                    ?: return@onEach
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
            .flowOn(Dispatchers.IO)
            .launchIn(activity.lifecycleScope)
    }

    companion object {
        private const val SEND_SCROBBLE_EVENT_TIME = 5000L
    }
}