package ru.stersh.youamp.core.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

private const val ONE_HOUR_MS = 3600000L
private val utcTimeZone = TimeZone.getTimeZone("UTC")

@SuppressLint("SimpleDateFormat")
private val minuteTimeFormat = SimpleDateFormat("mm:ss").apply {
    timeZone = utcTimeZone
}

@SuppressLint("SimpleDateFormat")
private val hourTimeFormat = SimpleDateFormat("hh:mm:ss").apply {
    timeZone = utcTimeZone
}

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