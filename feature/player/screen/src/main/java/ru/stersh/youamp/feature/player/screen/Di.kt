package ru.stersh.youamp.feature.player.screen

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.player.screen.ui.PlayerScreenViewModel

val playerScreenModule = module {
    viewModel { PlayerScreenViewModel(get(), get(), get(), get(), get(), get()) }
}