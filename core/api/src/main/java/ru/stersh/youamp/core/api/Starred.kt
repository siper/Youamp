package ru.stersh.youamp.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Starred2Response(
    @Json(name = "starred2") val starred2Result: Starred2Result
)

@JsonClass(generateAdapter = true)
data class Starred2Result(
    @Json(name = "album") val album: List<Album>?,
    @Json(name = "artist") val artist: List<Artist>?,
    @Json(name = "song") val song: List<Song>?,
)