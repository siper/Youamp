package ru.stresh.youamp.feature.about

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.about.ui.AboutAppViewModel

val aboutModule = module {
    viewModel { AboutAppViewModel(get()) }
}