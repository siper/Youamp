package ru.stresh.youamp.core.properties.app

import android.net.Uri

data class AppProperties(
    val name: String,
    val version: String,
    val githubUri: Uri,
    val fdroidUri: Uri,
    val crwodinUri: Uri
)
