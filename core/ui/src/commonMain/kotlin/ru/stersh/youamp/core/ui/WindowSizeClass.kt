package ru.stersh.youamp.core.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val DesktopWindowClassSize = WindowSizeClass.calculateFromSize(DpSize(1280.dp, 800.dp))

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val MobileWindowClassSize = WindowSizeClass.calculateFromSize(DpSize(1080.dp, 2400.dp))

val LocalWindowSizeClass = compositionLocalOf<WindowSizeClass> {
    MobileWindowClassSize
}