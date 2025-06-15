package ru.stersh.youamp.feature.song.random

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.song.random.ui.RandomSongsViewModel

val songRandomModule =
    module {
        viewModel {
            RandomSongsViewModel(
                get(),
                get(),
            )
        }
    }
