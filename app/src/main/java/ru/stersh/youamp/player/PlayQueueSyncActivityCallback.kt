package ru.stersh.youamp.player

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.utils.EmptyActivityLifecycleCallback
import ru.stersh.youamp.main.ui.MainActivity

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