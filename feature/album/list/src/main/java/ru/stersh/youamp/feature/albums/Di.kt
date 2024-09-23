package ru.stersh.youamp.feature.albums

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.albums.data.AlbumsRepositoryImpl
import ru.stersh.youamp.feature.albums.domain.AlbumsRepository
import ru.stersh.youamp.feature.albums.ui.AlbumsViewModel

val albumsModule = module {
    single<AlbumsRepository> { AlbumsRepositoryImpl(get()) }
    viewModel { AlbumsViewModel(get(), get()) }
}