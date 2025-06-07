package ru.stersh.youamp.core.ui

enum class Platform(
    val mobile: Boolean
) {
    Android(mobile = true),
    Desktop(mobile = false)
}

expect val currentPlatform: Platform