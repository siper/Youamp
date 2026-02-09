package ru.stersh.youamp.feature.artist.info

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.feature.artist.info.data.ArtistFavoriteRepositoryImpl
import ru.stersh.youamp.feature.artist.info.data.ArtistInfoRepositoryImpl
import ru.stersh.youamp.feature.artist.info.domain.ArtistFavoriteRepository
import ru.stersh.youamp.feature.artist.info.domain.ArtistInfoRepository
import ru.stersh.youamp.feature.artist.info.ui.ArtistInfoViewModel
import ru.stersh.youamp.shared.favorites.ArtistFavoritesStorage
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

val artistInfoModule =
    module {
        single<ArtistInfoRepository> {
            ArtistInfoRepositoryImpl(
                get<ru.stersh.youamp.core.api.ApiProvider>(),
                get<ArtistFavoritesStorage>(),
            )
        }
        single<ArtistFavoriteRepository> { ArtistFavoriteRepositoryImpl(get<ArtistFavoritesStorage>()) }
        viewModel { (id: String) ->
            ArtistInfoViewModel(
                id,
                get<ArtistInfoRepository>(),
                get<ArtistFavoriteRepository>(),
                get<PlayerQueueAudioSourceManager>(),
                get<Player>(),
            )
        }
    }
