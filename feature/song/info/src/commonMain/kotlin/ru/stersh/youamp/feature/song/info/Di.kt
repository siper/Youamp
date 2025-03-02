package ru.stersh.youamp.feature.song.info

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.song.info.ui.SongInfoViewModel

val songInfoModule = module {
    viewModel { SongInfoViewModel(get(), get(), get()) }
}