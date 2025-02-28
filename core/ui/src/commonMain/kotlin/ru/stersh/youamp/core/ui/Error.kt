package ru.stersh.youamp.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import youamp.core.ui.generated.resources.Res
import youamp.core.ui.generated.resources.error_button_title
import youamp.core.ui.generated.resources.error_message
import youamp.core.ui.generated.resources.error_title


@Composable
fun ErrorLayout(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Error(
            onRetry = onRetry,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun Error(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            text = stringResource(Res.string.error_title)
        )

        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            text = stringResource(Res.string.error_message)
        )

        Button(onClick = onRetry) {
            Text(text = stringResource(Res.string.error_button_title))
        }
    }
}

@Composable
@Preview
private fun ErrorLayoutPreview() {
    YouampPlayerTheme {
        Scaffold {
            ErrorLayout(
                onRetry = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}