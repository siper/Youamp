package ru.stersh.youamp.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.withContext
import ru.stersh.youamp.shared.player.android.MusicService
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher

internal class PlayerProviderImpl(private val context: Context) : PlayerProvider {

    override suspend fun get(): Player = withContext(PlayerDispatcher) {
        val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
        return@withContext MediaController
            .Builder(context, sessionToken)
            .buildAsync()
            .await()
    }
}