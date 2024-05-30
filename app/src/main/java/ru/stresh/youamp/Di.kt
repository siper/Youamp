package ru.stresh.youamp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.stersh.youamp.shared.player.playerSharedModule
import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.core.room.roomModule
import ru.stresh.youamp.feature.album.albumInfoModule
import ru.stresh.youamp.feature.albums.albumsModule
import ru.stresh.youamp.feature.artists.artistsModule
import ru.stresh.youamp.feature.player.mini.playerMiniModule
import ru.stresh.youamp.feature.player.queue.playerQueueScreenModule
import ru.stresh.youamp.feature.player.screen.playerScreenModule
import ru.stresh.youamp.feature.playlists.playlistListModule
import ru.stresh.youamp.feature.search.searchModule
import ru.stresh.youamp.feature.server.create.serverCreateModule
import ru.stresh.youamp.feature.server.list.serverListModule
import ru.stresh.youamp.main.data.AvatarUrlRepositoryImpl
import ru.stresh.youamp.main.data.ServerExistRepositoryImpl
import ru.stresh.youamp.main.domain.AvatarUrlRepository
import ru.stresh.youamp.main.domain.ServerExistRepository
import ru.stresh.youamp.main.ui.MainViewModel

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
    serverCreateModule,
    serverListModule,
    playlistListModule,
    searchModule
)

private val impl = module {
    single<ApiProvider> { ApiProviderImpl(get()) }
}

private val main = module {
    factory<ServerExistRepository> { ServerExistRepositoryImpl(get()) }
    single<AvatarUrlRepository> { AvatarUrlRepositoryImpl(get()) }
    viewModel { MainViewModel(get(), get()) }
}