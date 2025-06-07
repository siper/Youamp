package ru.stersh.youamp.core.player

data class PlayerProgress(
    val currentTimeMs: Long,
    val totalTimeMs: Long,
    val currentTime: String,
    val totalTime: String,
) {
    val percent: Float = (currentTimeMs.toFloat() / totalTimeMs).takeIf { !it.isNaN() } ?: 0f
}
