package ru.stresh.youamp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.core.room.roomModule
import ru.stresh.youamp.main.data.ServerExistRepositoryImpl
import ru.stresh.youamp.main.domain.ServerExistRepository
import ru.stresh.youamp.feature.album.albumInfoModule
import ru.stresh.youamp.feature.player.playerFeatureModule
import ru.stresh.youamp.feature.server.create.serverCreateModule
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
    ru.stersh.youamp.shared.player.playerSharedModule
)

private val feature = listOf(
    playerFeatureModule,
    albumInfoModule,
    serverCreateModule
)

private val impl = module {
    single<ApiProvider> { ApiProviderImpl(get()) }
}

private val main = module {
    factory<ServerExistRepository> { ServerExistRepositoryImpl(get()) }
    viewModel { MainViewModel(get()) }
}