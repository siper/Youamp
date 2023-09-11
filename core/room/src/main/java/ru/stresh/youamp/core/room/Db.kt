package ru.stresh.youamp.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.stresh.youamp.core.room.server.SubsonicServerDao
import ru.stresh.youamp.core.room.server.SubsonicServerDb

@Database(
    entities = [SubsonicServerDb::class],
    version = 1,
    exportSchema = false,
)
abstract class Database : RoomDatabase() {

    abstract fun subsonicServerDao(): SubsonicServerDao
}