package ru.stersh.youamp.core.db.server

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "server_subsonic")
data class SubsonicServerDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String,
    val name: String,
    val username: String,
    val password: String,
    val isActive: Boolean,
    val authType: String,
)
