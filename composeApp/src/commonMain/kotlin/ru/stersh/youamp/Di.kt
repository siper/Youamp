package ru.stersh.youamp

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.album.info.albumInfoModule
import ru.stersh.youamp.feature.album.list.albumListModule
import ru.stersh.youamp.feature.artist.info.artistInfoModule
import ru.stersh.youamp.feature.artist.list.artistListModule
import ru.stersh.youamp.feature.main.mainModule
import ru.stersh.youamp.feature.personal.personalModule
import ru.stersh.youamp.feature.player.mini.playerMiniModule
import ru.stersh.youamp.feature.player.queue.playerQueueScreenModule
import ru.stersh.youamp.feature.player.screen.playerScreenModule
import ru.stersh.youamp.feature.playlist.info.playlistInfoModule
import ru.stersh.youamp.feature.playlist.list.playlistListModule
import ru.stersh.youamp.feature.search.searchModule
import ru.stersh.youamp.feature.server.create.serverCreateModule
import ru.stersh.youamp.feature.server.list.serverListModule
import ru.stersh.youamp.feature.song.info.songInfoModule
import ru.stersh.youamp.main.domain.AvatarUrlRepository
import ru.stersh.youamp.main.domain.ServerExistRepository
import ru.stersh.youamp.main.ui.MainViewModel
import ru.stersh.youamp.player.ApiSonicPlayQueueSyncer
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.core.db.dbModule
import ru.stresh.youamp.core.player.playerCoreModule
import ru.stresh.youamp.core.propertiesModule
import ru.stresh.youamp.feature.about.aboutModule
import ru.stresh.youamp.feature.album.favorites.albumFavoritesModule
import ru.stresh.youamp.feature.artist.favorites.artistFavoritesModule
import ru.stresh.youamp.feature.explore.exploreModule
import ru.stresh.youamp.feature.library.libraryModule
import ru.stresh.youamp.feature.song.favorites.songFavoritesModule
import ru.stresh.youamp.feature.song.random.songRandomModule
import ru.stresh.youamp.shared.favorites.favoritesSharedModule
import ru.stresh.youamp.shared.queue.queueSharedModule
import ru.stresh.youamp.shared.song.random.songRandomSharedModule


private val core = listOf(
    propertiesModule,
    dbModule,
    playerCoreModule
)

private val shared = listOf(
    queueSharedModule,
    favoritesSharedModule,
    songRandomSharedModule
)

private val feature = listOf(
    playerMiniModule,
    playerScreenModule,
    playerQueueScreenModule,
    albumInfoModule,
    albumFavoritesModule,
    albumListModule,
    artistListModule,
    artistInfoModule,
    artistFavoritesModule,
    serverCreateModule,
    serverListModule,
    playlistListModule,
    playlistInfoModule,
    searchModule,
    songFavoritesModule,
    songInfoModule,
    songRandomModule,
    aboutModule,
    personalModule,
    exploreModule,
    mainModule,
    libraryModule
)

private val impl = module {
    single<ApiProvider> { ApiProviderImpl(get()) }
    single { ApiSonicPlayQueueSyncer(get(), get()) }
}

private val main = module {
    factory<ServerExistRepository> {
        ru.stersh.youamp.main.data.ServerExistRepositoryImpl(
            get()
        )
    }
    single<AvatarUrlRepository> {
        ru.stersh.youamp.main.data.AvatarUrlRepositoryImpl(
            get()
        )
    }

    viewModel { MainViewModel(get(), get()) }
}

val diModules: List<Module> = core + shared + feature + main + impl