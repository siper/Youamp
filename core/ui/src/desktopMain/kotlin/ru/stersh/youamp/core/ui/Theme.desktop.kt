package ru.stersh.youamp.core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun getColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    return if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
}