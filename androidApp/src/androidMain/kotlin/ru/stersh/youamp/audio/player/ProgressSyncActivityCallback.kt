package ru.stersh.youamp.audio.player

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
import ru.stersh.youamp.EmptyActivityLifecycleCallback
import ru.stersh.youamp.MainActivity
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.player.ScrobbleSender

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
                    .first()
                    ?.id
                    ?: return@onEach
                if (currentItemId != scrobbleSender?.id) {
                    scrobbleSender = ScrobbleSender(currentItemId, apiProvider)
                }
                val sender = scrobbleSender ?: return@onEach
                if (progress.currentTimeMs >= (progress.totalTimeMs / 2) && !sender.scrobbleSent) {
                    activity.lifecycleScope.launch {
                        sender.trySendScrobble()
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(activity.lifecycleScope)
    }
}