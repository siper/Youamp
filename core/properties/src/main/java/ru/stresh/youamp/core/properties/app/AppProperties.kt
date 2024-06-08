package ru.stresh.youamp.core.properties.app

import android.net.Uri

data class AppProperties(
    val name: String,
    val version: String,
    val googlePlayAppUri: Uri,
    val googlePlayBrowserUri: Uri,
    val githubUri: Uri,
    val fdroidUri: Uri,
    val crwodinUri: Uri
)
