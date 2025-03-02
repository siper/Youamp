package ru.stersh.youamp.feature.server.create

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.server.create.data.ServerRepositoryImpl
import ru.stersh.youamp.feature.server.create.domain.ServerRepository
import ru.stersh.youamp.feature.server.create.ui.ServerCreateViewModel

val serverCreateModule = module {
    factory<ServerRepository> { ServerRepositoryImpl(get()) }
    viewModel { (serverId: Long?) ->
        ServerCreateViewModel(serverId, get())
    }
}