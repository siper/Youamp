package ru.stersh.youamp.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayQueueResponse(
    @Json(name = "playQueue")
    val playQueue: PlayQueue,
)

@JsonClass(generateAdapter = true)
data class PlayQueue(
    @Json(name = "current")
    val current: String?,
    @Json(name = "position")
    val position: Long?,
    @Json(name = "username")
    val username: String,
    @Json(name = "changed")
    val changed: String,
    @Json(name = "changedBy")
    val changedBy: String,
    @Json(name = "entry")
    val entry: List<Song>
)