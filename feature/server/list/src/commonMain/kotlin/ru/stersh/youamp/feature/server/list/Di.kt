package ru.stersh.youamp.feature.server.list

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.server.list.data.ServerListRepositoryImpl
import ru.stersh.youamp.feature.server.list.domain.ServerListRepository
import ru.stersh.youamp.feature.server.list.ui.ServerListViewModel

val serverListModule =
    module {
        single<ServerListRepository> { ServerListRepositoryImpl(get()) }
        viewModel { ServerListViewModel(get()) }
    }
