package ru.stersh.youamp.feature.playlists

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.playlists.data.PlaylistsRepositoryImpl
import ru.stersh.youamp.feature.playlists.domain.PlaylistsRepository
import ru.stersh.youamp.feature.playlists.ui.PlaylistsViewModel

val playlistListModule = module {
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get()) }
    viewModel { PlaylistsViewModel(get()) }
}