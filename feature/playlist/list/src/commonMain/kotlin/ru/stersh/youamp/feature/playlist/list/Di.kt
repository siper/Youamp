package ru.stersh.youamp.feature.playlist.list

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.playlist.list.data.PlaylistsRepositoryImpl
import ru.stersh.youamp.feature.playlist.list.domain.PlaylistsRepository
import ru.stersh.youamp.feature.playlist.list.ui.PlaylistsViewModel

val playlistListModule = module {
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get()) }
    viewModel { PlaylistsViewModel(get()) }
}