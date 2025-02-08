package ru.stersh.youamp.shared.player.utils

import android.os.Handler
import android.os.HandlerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.android.asCoroutineDispatcher

val PlayerThread = HandlerThread("PlayerThread").apply { start() }
val PlayerHandler = Handler(PlayerThread.looper)
val PlayerDispatcher = PlayerHandler.asCoroutineDispatcher("PlayerDispatcher")
val PlayerScope = CoroutineScope(SupervisorJob() + PlayerDispatcher)