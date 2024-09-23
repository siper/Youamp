package ru.stersh.youamp.feature.album

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.album.data.AlbumInfoRepositoryImpl
import ru.stersh.youamp.feature.album.domain.AlbumInfoRepository
import ru.stersh.youamp.feature.album.ui.AlbumInfoViewModel

val albumInfoModule = module {
    single<AlbumInfoRepository> { AlbumInfoRepositoryImpl(get()) }
    viewModel { (id: String) ->
        AlbumInfoViewModel(id, get(), get())
    }
}