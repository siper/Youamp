package ru.stersh.youamp.feature.player.queue

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.player.queue.ui.PlayerQueueViewModel

val playerQueueScreenModule = module {
    viewModel { PlayerQueueViewModel(get(), get(), get()) }
}