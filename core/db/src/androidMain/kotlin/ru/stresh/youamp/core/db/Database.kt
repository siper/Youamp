package ru.stresh.youamp.core.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<Database> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("app.db")
    return Room.databaseBuilder<Database>(
        context = appContext,
        name = dbFile.absolutePath
    )
}