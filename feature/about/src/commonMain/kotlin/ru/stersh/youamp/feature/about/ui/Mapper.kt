package ru.stersh.youamp.feature.about.ui

import ru.stersh.youamp.core.properties.app.AppProperties

internal fun AppProperties.toUiState(): AboutStateUi =
    AboutStateUi(
        name = name,
        version = version,
        githubUri = githubUrl,
        fdroidUri = fdroidUrl,
        crwodinUri = crwodinUrl,
    )
