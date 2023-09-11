package ru.stresh.youamp.feature.player

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.player.big.ui.PlayerScreenViewModel
import ru.stresh.youamp.feature.player.mini.ui.MiniPlayerViewModel

val playerFeatureModule = module {
    viewModel { PlayerScreenViewModel(get(), get(), get(), get()) }
    viewModel { MiniPlayerViewModel(get(), get(), get(), get()) }
}