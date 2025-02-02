package ru.stersh.youamp

import android.app.Application
import android.content.Context
import androidx.core.net.toUri
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.room.roomModule
import ru.stersh.youamp.feature.album.albumInfoModule
import ru.stersh.youamp.feature.albums.albumsModule
import ru.stersh.youamp.feature.artist.artistInfoModule
import ru.stersh.youamp.feature.artists.artistsModule
import ru.stersh.youamp.feature.main.mainModule
import ru.stersh.youamp.feature.personal.personalModule
import ru.stersh.youamp.feature.player.mini.playerMiniModule
import ru.stersh.youamp.feature.player.queue.playerQueueScreenModule
import ru.stersh.youamp.feature.player.screen.playerScreenModule
import ru.stersh.youamp.feature.playlist.playlistInfoModule
import ru.stersh.youamp.feature.playlists.playlistListModule
import ru.stersh.youamp.feature.search.searchModule
import ru.stersh.youamp.feature.server.create.serverCreateModule
import ru.stersh.youamp.feature.server.list.serverListModule
import ru.stersh.youamp.feature.song.info.songInfoModule
import ru.stersh.youamp.main.data.AvatarUrlRepositoryImpl
import ru.stersh.youamp.main.data.ServerExistRepositoryImpl
import ru.stersh.youamp.main.domain.AvatarUrlRepository
import ru.stersh.youamp.main.domain.ServerExistRepository
import ru.stersh.youamp.main.ui.MainViewModel
import ru.stersh.youamp.shared.player.playerSharedModule
import ru.stresh.youamp.core.properties.app.AppProperties
import ru.stresh.youamp.core.propertiesModule
import ru.stresh.youamp.feature.about.aboutModule
import ru.stresh.youamp.feature.explore.exploreModule
import ru.stresh.youamp.feature.favorite.list.favoriteListModule
import ru.stresh.youamp.feature.library.libraryModule

internal fun setupDi(application: Application) {
    startKoin {
        androidContext(application)
        modules(core + shared + feature + main + impl)
    }
}

private val core = listOf(
    propertiesModule,
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
    favoriteListModule,
    aboutModule,
    personalModule,
    exploreModule,
    mainModule,
    libraryModule,
    songInfoModule
)

private val impl = module {
    single {
        AppProperties(
            name = get<Context>().getString(R.string.app_name),
            version = BuildConfig.VERSION_NAME,
            googlePlayAppUri = "market://details?id=ru.stersh.youamp".toUri(),
            googlePlayBrowserUri = "https://play.google.com/store/apps/details?id=ru.stersh.youamp".toUri(),
            githubUri = "https://github.com/siper/Youamp".toUri(),
            fdroidUri = "https://f-droid.org/packages/ru.stersh.youamp/".toUri(),
            crwodinUri = "https://crowdin.com/project/youamp".toUri()
        )
    }
    single<ApiProvider> { ApiProviderImpl(get()) }
}

private val main = module {
    factory<ServerExistRepository> { ServerExistRepositoryImpl(get()) }
    single<AvatarUrlRepository> { AvatarUrlRepositoryImpl(get()) }
    viewModel { MainViewModel(get(), get()) }
}