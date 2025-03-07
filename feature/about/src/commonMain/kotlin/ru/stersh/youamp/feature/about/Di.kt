package ru.stersh.youamp.feature.about

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.about.ui.AboutAppViewModel

val aboutModule = module {
    viewModel { AboutAppViewModel(get()) }
}