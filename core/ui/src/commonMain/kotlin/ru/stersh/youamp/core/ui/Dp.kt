package ru.stersh.youamp.core.ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun Float.toDp(density: Density): Dp = with(density) { toDp() }

fun Int.toDp(density: Density): Dp = with(density) { toDp() }

fun Dp.toPx(density: Density): Float = with(density) { toPx() }

fun Dp.roundToPx(density: Density): Int = with(density) { roundToPx() }
