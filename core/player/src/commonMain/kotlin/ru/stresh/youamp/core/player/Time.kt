package ru.stresh.youamp.core.player

import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

private const val ONE_HOUR_MS = 3600000L

private val hourTimeFormat = LocalTime.Format {
    hour()
    char(':')
    minute()
    char(':')
    second()
}

private val minuteTimeFormat = LocalTime.Format {
    minute()
    char(':')
    second()
}

fun formatSongDuration(time: Long): String {
    val dateTime = LocalTime.fromMillisecondOfDay(time.toInt())
    return if (time >= ONE_HOUR_MS) {
        hourTimeFormat.format(dateTime)
    } else {
        minuteTimeFormat.format(dateTime)
    }
}

fun formatSongDuration(time: Int): String {
    return formatSongDuration(time.toLong())
}