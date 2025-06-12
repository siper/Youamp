package ru.stersh.youamp.core.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val DesktopWindowClassSize = WindowSizeClass.calculateFromSize(DpSize(1280.dp, 800.dp))

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val MobileWindowClassSize = WindowSizeClass.calculateFromSize(
    size = DpSize(
        1080.dp,
        2400.dp
    ),
    supportedWidthSizeClasses = setOf(WindowWidthSizeClass.Compact),
    supportedHeightSizeClasses = setOf(WindowHeightSizeClass.Medium)
)

val LocalWindowSizeClass = compositionLocalOf<WindowSizeClass> {
    MobileWindowClassSize
}

val isCompactWidth: Boolean
    @Composable
    get() = LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Compact

val isMediumWidth: Boolean
    @Composable
    get() = LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Medium

val isExpandedWidth: Boolean
    @Composable
    get() = LocalWindowSizeClass.current.widthSizeClass == WindowWidthSizeClass.Expanded

val isCompactHeight: Boolean
    @Composable
    get() = LocalWindowSizeClass.current.heightSizeClass == WindowHeightSizeClass.Compact

val isMediumHeight: Boolean
    @Composable
    get() = LocalWindowSizeClass.current.heightSizeClass == WindowHeightSizeClass.Medium

val isExpandedHeight: Boolean
    @Composable
    get() = LocalWindowSizeClass.current.heightSizeClass == WindowHeightSizeClass.Expanded