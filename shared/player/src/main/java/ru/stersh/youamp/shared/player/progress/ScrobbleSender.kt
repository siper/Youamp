package ru.stersh.youamp.shared.player.progress

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stersh.youamp.core.api.provider.ApiProvider
import timber.log.Timber

internal class ScrobbleSender(
    val id: String,
    private val apiProvider: ApiProvider,
) {
    private var scrobbleSent = false
    private var submissionSent = false

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
