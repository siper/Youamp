package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun EmptyLayout(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Empty(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun Empty(
    modifier: Modifier = Modifier
) {
    Text(
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.outline,
        text = stringResource(R.string.empty_state_title),
        modifier = modifier
    )
}