package ru.stersh.youamp.core.ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp


fun Float.toDp(density: Density): Dp {
    return with(density) { toDp() }
}

fun Int.toDp(density: Density): Dp {
    return with(density) { toDp() }
}

fun Dp.toPx(density: Density): Float {
    return with(density) { toPx() }
}

fun Dp.roundToPx(density: Density): Int {
    return with(density) { roundToPx() }
}