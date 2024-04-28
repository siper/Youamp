package ru.stresh.youamp.feature.playlists

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.playlists.data.PlaylistsRepositoryImpl
import ru.stresh.youamp.feature.playlists.domain.PlaylistsRepository
import ru.stresh.youamp.feature.playlists.ui.PlaylistsViewModel

val playlistListModule = module {
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get()) }
    viewModel { PlaylistsViewModel(get()) }
}