package ru.stresh.youamp.feature.song.random

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.song.random.ui.RandomSongsViewModel

val songRandomModule = module {
    viewModel { RandomSongsViewModel(get(), get()) }
}