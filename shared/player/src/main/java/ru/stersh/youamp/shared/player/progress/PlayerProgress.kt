package ru.stersh.youamp.shared.player.progress

data class PlayerProgress(
    val currentTimeMs: Long,
    val totalTimeMs: Long,
    val currentTime: String,
    val totalTime: String,
) {
    val progressPercent: Float
        get() = currentTimeMs.toFloat() / totalTimeMs
}
