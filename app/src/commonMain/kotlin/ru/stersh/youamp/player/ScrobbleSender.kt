package ru.stersh.youamp.player

import co.touchlab.kermit.Logger
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stersh.youamp.core.api.ApiProvider

class ScrobbleSender(
    val id: String,
    private val apiProvider: ApiProvider,
) {
    var scrobbleSent = false
        private set

    private val sendMutex = Mutex()

    suspend fun trySendScrobble() = sendMutex.withLock {
        if (scrobbleSent) {
            return@withLock
        }
        runCatching {
            apiProvider
                .getApi()
                .scrobble(
                    id = id,
                    time = System.currentTimeMillis(),
                )
            scrobbleSent = true
        }.onFailure { Logger.w(it) { "trySendScrobble error" } }
    }
}
