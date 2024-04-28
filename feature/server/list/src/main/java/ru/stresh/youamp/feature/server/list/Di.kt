package ru.stresh.youamp.feature.server.list

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.server.list.data.ServerListRepositoryImpl
import ru.stresh.youamp.feature.server.list.domain.ServerListRepository
import ru.stresh.youamp.feature.server.list.ui.ServerListViewModel

val serverListModule = module {
    single<ServerListRepository> { ServerListRepositoryImpl(get()) }
    viewModel { ServerListViewModel(get()) }
}