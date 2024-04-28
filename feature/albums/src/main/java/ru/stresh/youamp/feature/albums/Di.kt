package ru.stresh.youamp.feature.albums

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.albums.ui.AlbumsViewModel

val albumsModule = module {
    viewModel { AlbumsViewModel(get()) }
}