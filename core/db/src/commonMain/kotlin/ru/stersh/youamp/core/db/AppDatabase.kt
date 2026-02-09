package ru.stersh.youamp.core.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import ru.stersh.youamp.core.db.server.SubsonicServerDao
import ru.stersh.youamp.core.db.server.SubsonicServerDb

@Database(
    entities = [SubsonicServerDb::class],
    version = 2,
    exportSchema = false,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subsonicServerDao(): SubsonicServerDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
