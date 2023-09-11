package ru.stresh.youamp.core.utils

import java.text.SimpleDateFormat
import java.util.Date

private const val ONE_HOUR_MS = 3600000L
private val minuteTimeFormat = SimpleDateFormat("mm:ss")
private val hourTimeFormat = SimpleDateFormat("hh:mm:ss")

fun formatSongDuration(time: Long): String {
    return if (time >= ONE_HOUR_MS) {
        hourTimeFormat.format(Date(time))
    } else {
        minuteTimeFormat.format(Date(time))
    }
}

fun formatSongDuration(time: Int): String {
    return formatSongDuration(time.toLong())
}