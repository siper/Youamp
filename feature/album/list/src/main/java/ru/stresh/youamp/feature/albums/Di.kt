package ru.stresh.youamp.feature.albums

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.albums.data.AlbumsRepositoryImpl
import ru.stresh.youamp.feature.albums.domain.AlbumsRepository
import ru.stresh.youamp.feature.albums.ui.AlbumsViewModel

val albumsModule = module {
    single<AlbumsRepository> { AlbumsRepositoryImpl(get()) }
    viewModel { AlbumsViewModel(get()) }
}