package ru.stersh.youamp.player

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stresh.youamp.core.api.ApiProvider
import timber.log.Timber

internal class ScrobbleSender(
    val id: String,
    private val apiProvider: ApiProvider,
) {
    var scrobbleSent = false
        private set
    var submissionSent = false
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
        }.onFailure { Timber.w(it) }
    }

    suspend fun trySendSubmission() = sendMutex.withLock {
        if (submissionSent) {
            return@withLock
        }
        runCatching {
            apiProvider
                .getApi()
                .scrobble(
                    id = id,
                    time = System.currentTimeMillis(),
                    submission = true,
                )
            submissionSent = true
        }.onFailure { Timber.w(it) }
    }
}
