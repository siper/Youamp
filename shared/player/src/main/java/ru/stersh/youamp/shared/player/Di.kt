package ru.stersh.youamp.shared.player

import org.koin.dsl.module
import ru.stersh.youamp.shared.player.android.ApiSonicPlayQueueSyncer
import ru.stersh.youamp.shared.player.controls.PlayerControls
import ru.stersh.youamp.shared.player.controls.PlayerControlsImpl
import ru.stersh.youamp.shared.player.favorites.CurrentSongFavorites
import ru.stersh.youamp.shared.player.favorites.CurrentSongFavoritesImpl
import ru.stersh.youamp.shared.player.metadata.CurrentSongInfoStore
import ru.stersh.youamp.shared.player.metadata.CurrentSongInfoStoreImpl
import ru.stersh.youamp.shared.player.mode.PlayerMode
import ru.stersh.youamp.shared.player.mode.PlayerModeImpl
import ru.stersh.youamp.shared.player.progress.PlayerProgressStore
import ru.stersh.youamp.shared.player.progress.PlayerProgressStoreImpl
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManagerImpl
import ru.stersh.youamp.shared.player.queue.PlayerQueueManager
import ru.stersh.youamp.shared.player.queue.PlayerQueueManagerImpl
import ru.stersh.youamp.shared.player.state.PlayStateStore
import ru.stersh.youamp.shared.player.state.PlayStateStoreImpl

val playerSharedModule = module {
    single<PlayerQueueAudioSourceManager> { PlayerQueueAudioSourceManagerImpl(get(), get()) }
    single<PlayerControls> { PlayerControlsImpl(get()) }
    single<PlayStateStore> { PlayStateStoreImpl(get()) }
    single<PlayerProgressStore> { PlayerProgressStoreImpl(get(), get()) }
    single<CurrentSongInfoStore> { CurrentSongInfoStoreImpl(get()) }
    single<PlayerQueueManager> { PlayerQueueManagerImpl(get()) }
    single { ApiSonicPlayQueueSyncer(get()) }
    single<PlayerMode> { PlayerModeImpl(get()) }
    single<CurrentSongFavorites> { CurrentSongFavoritesImpl(get(), get()) }
}
