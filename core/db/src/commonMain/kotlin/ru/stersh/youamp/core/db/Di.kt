package ru.stersh.youamp.core.db

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val dbModule = module {
    single {
        get<RoomDatabase.Builder<AppDatabase>>()
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .setQueryCoroutineContext(Dispatchers.IO)
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single {
        get<AppDatabase>().subsonicServerDao()
    }
}