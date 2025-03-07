package ru.stersh.youamp.core.db

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<Database> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "app.db")
    return Room.databaseBuilder<Database>(
        name = dbFile.absolutePath,
    )
}