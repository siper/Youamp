package ru.stersh.youamp.shared.favorites

import org.koin.dsl.binds
import org.koin.dsl.module

val favoritesSharedModule = module {
    single { FavoritesStorageImpl(get()) } binds arrayOf(
        SongFavoritesStorage::class,
        AlbumFavoritesStorage::class,
        ArtistFavoritesStorage::class
    )
}