package ru.stersh.youamp.feature.playlist

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.playlist.data.PlaylistInfoRepositoryImpl
import ru.stersh.youamp.feature.playlist.domain.PlaylistInfoRepository
import ru.stersh.youamp.feature.playlist.ui.PlaylistInfoViewModel

val playlistInfoModule = module {
    single<PlaylistInfoRepository> { PlaylistInfoRepositoryImpl(get(), get()) }
    viewModel { (id: String) ->
        PlaylistInfoViewModel(id, get(), get(), get())
    }
}