package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import youamp.core.ui.generated.resources.Res
import youamp.core.ui.generated.resources.empty_state_title

@Composable
fun EmptyLayout(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Empty(
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun Empty(modifier: Modifier = Modifier) {
    Text(
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        text = stringResource(Res.string.empty_state_title),
        modifier = modifier,
    )
}

@Composable
@Preview
private fun EmptyLayoutPreview() {
    YouampPlayerTheme {
        Scaffold {
            EmptyLayout(
                modifier = Modifier.padding(it),
            )
        }
    }
}
