package ru.stersh.youamp.feature.artist.favorites

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.feature.artist.favorites.data.FavoriteArtistsRepositoryImpl
import ru.stersh.youamp.feature.artist.favorites.domain.FavoriteArtistsRepository
import ru.stersh.youamp.feature.artist.favorites.ui.FavoriteArtistViewModel
import ru.stersh.youamp.shared.favorites.ArtistFavoritesStorage
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

val artistFavoritesModule =
    module {
        single<FavoriteArtistsRepository> { FavoriteArtistsRepositoryImpl(get<ArtistFavoritesStorage>()) }
        viewModel {
            FavoriteArtistViewModel(
                get<FavoriteArtistsRepository>(),
                get<PlayerQueueAudioSourceManager>(),
                get<Player>(),
            )
        }
    }
