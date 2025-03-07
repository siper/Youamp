package ru.stersh.youamp.shared.song.random

import org.koin.dsl.module

val songRandomSharedModule = module {
    single<SongRandomStorage> { SongRandomStorageImpl(get()) }
}