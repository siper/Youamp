package ru.stersh.youamp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.room.roomModule
import ru.stersh.youamp.feature.album.albumInfoModule
import ru.stersh.youamp.feature.albums.albumsModule
import ru.stersh.youamp.feature.artist.artistInfoModule
import ru.stersh.youamp.feature.artists.artistsModule
import ru.stersh.youamp.feature.player.mini.playerMiniModule
import ru.stersh.youamp.feature.player.queue.playerQueueScreenModule
import ru.stersh.youamp.feature.player.screen.playerScreenModule
import ru.stersh.youamp.feature.playlist.playlistInfoModule
import ru.stersh.youamp.feature.playlists.playlistListModule
import ru.stersh.youamp.feature.search.searchModule
import ru.stersh.youamp.feature.server.create.serverCreateModule
import ru.stersh.youamp.feature.server.list.serverListModule
import ru.stersh.youamp.main.data.AvatarUrlRepositoryImpl
import ru.stersh.youamp.main.data.ServerExistRepositoryImpl
import ru.stersh.youamp.main.domain.AvatarUrlRepository
import ru.stersh.youamp.main.domain.ServerExistRepository
import ru.stersh.youamp.main.ui.MainViewModel
import ru.stersh.youamp.shared.player.playerSharedModule
import ru.stresh.youamp.feature.favorite.list.favoriteListModule

internal fun setupDi(application: Application) {
    startKoin {
        androidContext(application)
        modules(core + shared + feature + main + impl)
    }
}

private val core = listOf(
    roomModule
)

private val shared = listOf(
    playerSharedModule
)

private val feature = listOf(
    playerMiniModule,
    playerScreenModule,
    playerQueueScreenModule,
    albumInfoModule,
    albumsModule,
    artistsModule,
    artistInfoModule,
    serverCreateModule,
    serverListModule,
    playlistListModule,
    searchModule,
    playlistInfoModule,
    favoriteListModule
)

private val impl = module {
    single<ApiProvider> { ApiProviderImpl(get()) }
}

private val main = module {
    factory<ServerExistRepository> { ServerExistRepositoryImpl(get()) }
    single<AvatarUrlRepository> { AvatarUrlRepositoryImpl(get()) }
    viewModel { MainViewModel(get(), get()) }
}