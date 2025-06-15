package ru.stersh.youamp.feature.main

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.main.data.ServerInfoRepositoryImpl
import ru.stersh.youamp.feature.main.domain.ServerInfoRepository
import ru.stersh.youamp.feature.main.ui.MainViewModel

val mainModule =
    module {
        single<ServerInfoRepository> {
            ServerInfoRepositoryImpl(
                get(),
                get(),
            )
        }
        viewModel { MainViewModel(get()) }
    }
