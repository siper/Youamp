package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val BIG_SPACER = 16.dp
private val MEDIUM_SPACER = 8.dp
private val SMALL_SPACER = 4.dp

@Composable
fun VerticalBigSpacer() {
    Spacer(modifier = Modifier.height(BIG_SPACER))
}

@Composable
fun VerticalMediumSpacer() {
    Spacer(modifier = Modifier.height(MEDIUM_SPACER))
}

@Composable
fun VerticalSmallSpacer() {
    Spacer(modifier = Modifier.height(SMALL_SPACER))
}

@Composable
fun HorizontalBigSpacer() {
    Spacer(modifier = Modifier.width(BIG_SPACER))
}

@Composable
fun HorizontalMediumSpacer() {
    Spacer(modifier = Modifier.width(MEDIUM_SPACER))
}

@Composable
fun HorizontalSmallSpacer() {
    Spacer(modifier = Modifier.width(SMALL_SPACER))
}