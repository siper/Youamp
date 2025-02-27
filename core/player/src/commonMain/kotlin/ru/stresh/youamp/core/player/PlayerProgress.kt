package ru.stresh.youamp.core.player

data class PlayerProgress(
    val currentTimeMs: Long,
    val totalTimeMs: Long,
    val currentTime: String,
    val totalTime: String,
) {
    val progressPercent: Float = currentTimeMs.toFloat() / totalTimeMs
}
