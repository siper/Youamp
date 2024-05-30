package ru.stresh.youamp.feature.player.queue

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.player.queue.ui.PlayerQueueViewModel

val playerQueueScreenModule = module {
    viewModel { PlayerQueueViewModel(get(), get(), get()) }
}