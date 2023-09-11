package ru.stresh.youamp.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ErrorResponse(
    @Json(name = "subsonic-response")
    val data: ErrorResponseBody,
)

@JsonClass(generateAdapter = true)
internal data class ErrorResponseBody(
    @Json(name = "status")
    val status: String,
    @Json(name = "version")
    val version: String,
    @Json(name = "error")
    val error: Error,
)

@JsonClass(generateAdapter = true)
internal data class Error(
    @Json(name = "code")
    val code: Int,
    @Json(name = "message")
    val message: String,
)
