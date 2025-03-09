package ru.stersh.youamp.audio.player

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.stersh.youamp.EmptyActivityLifecycleCallback
import ru.stersh.youamp.MainActivity
import ru.stersh.youamp.player.ApiSonicPlayQueueSyncer

internal class PlayQueueSyncActivityCallback(
    private val apiSonicPlayQueueSyncer: ApiSonicPlayQueueSyncer
) : EmptyActivityLifecycleCallback() {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is MainActivity) {
            return
        }
        activity.lifecycleScope.launch {
            apiSonicPlayQueueSyncer.syncQueue()
        }
    }
}