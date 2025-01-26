package ru.stersh.youamp.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PlayAllFabButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = stringResource(R.string.play_all_title)
        )
        Text(text = stringResource(R.string.play_all_title))
    }
}

@Composable
@Preview
private fun PlayAllFabButtonPreview() {
    YouampPlayerTheme {
        PlayAllFabButton(
            onClick = {}
        )
    }
}