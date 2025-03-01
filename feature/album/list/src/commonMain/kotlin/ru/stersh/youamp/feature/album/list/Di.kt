package ru.stersh.youamp.feature.album.list

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.album.list.data.AlbumsRepositoryImpl
import ru.stersh.youamp.feature.album.list.domain.AlbumsRepository
import ru.stersh.youamp.feature.album.list.ui.AlbumsViewModel

val albumListModule = module {
    single<AlbumsRepository> { AlbumsRepositoryImpl(get()) }
    viewModel { AlbumsViewModel(get(), get()) }
}