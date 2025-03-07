package ru.stersh.youamp.core.db

import androidx.room.RoomDatabaseConstructor

expect object AppDatabaseConstructor : RoomDatabaseConstructor<Database> {
    override fun initialize(): Database
}