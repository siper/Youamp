package ru.stresh.youamp.core.room

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {
        single {
            Room
                .databaseBuilder(
                    androidContext(),
                    Database::class.java,
                    "app.db"
                )
                .build()
        }

        single {
            get<Database>().subsonicServerDao()
        }
}