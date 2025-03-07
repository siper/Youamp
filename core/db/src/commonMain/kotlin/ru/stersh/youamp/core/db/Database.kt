package ru.stersh.youamp.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.stersh.youamp.core.db.server.SubsonicServerDao
import ru.stersh.youamp.core.db.server.SubsonicServerDb

@Database(
    entities = [SubsonicServerDb::class],
    version = 1,
    exportSchema = false,
)
abstract class Database : RoomDatabase() {

    abstract fun subsonicServerDao(): SubsonicServerDao
}