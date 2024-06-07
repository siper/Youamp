package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun EmptyLayout(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Empty(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
    }
}

@Composable
fun Empty(
    modifier: Modifier = Modifier
) {
    Text(
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        text = stringResource(R.string.empty_state_title),
        modifier = modifier
    )
}