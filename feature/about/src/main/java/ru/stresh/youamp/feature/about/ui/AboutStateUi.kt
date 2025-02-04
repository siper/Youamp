package ru.stresh.youamp.feature.about.ui

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
internal data class AboutStateUi(
    val name: String,
    val version: String,
    val githubUri: Uri,
    val fdroidUri: Uri,
    val crwodinUri: Uri
)
