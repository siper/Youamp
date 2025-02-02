package ru.stersh.youamp.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val clickable = if (onClick == null) {
        Modifier
    } else {
        Modifier.clickable(onClick = onClick)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .then(clickable)
            .clip(MaterialTheme.shapes.medium)
            .padding(
                vertical = 24.dp,
                horizontal = 24.dp
            )
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
        )
        if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.requiredSize(18.dp)
            )
        }
    }
}