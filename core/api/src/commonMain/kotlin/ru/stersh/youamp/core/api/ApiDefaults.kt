package ru.stersh.youamp.core.api

import kotlin.time.Duration.Companion.minutes

object ApiDefaults {
    const val API_VERSION = "1.15.0"
    const val CLIENT_ID = "Youamp"

    val ReadTimeout = 1.minutes
    val ConnectTimeout = 1.minutes
}
