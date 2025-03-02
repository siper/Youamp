package ru.stersh.youamp.feature.playlist.info

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.playlist.info.data.PlaylistInfoRepositoryImpl
import ru.stersh.youamp.feature.playlist.info.domain.PlaylistInfoRepository
import ru.stersh.youamp.feature.playlist.info.ui.PlaylistInfoViewModel

val playlistInfoModule = module {
    single<PlaylistInfoRepository> { PlaylistInfoRepositoryImpl(get(), get()) }
    viewModel { (id: String) ->
        PlaylistInfoViewModel(id, get(), get(), get())
    }
}