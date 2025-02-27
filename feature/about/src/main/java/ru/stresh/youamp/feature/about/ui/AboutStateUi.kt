package ru.stresh.youamp.feature.about.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class AboutStateUi(
    val name: String,
    val version: String,
    val githubUri: String,
    val fdroidUri: String,
    val crwodinUri: String
)
