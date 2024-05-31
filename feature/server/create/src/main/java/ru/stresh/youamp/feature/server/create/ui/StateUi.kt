package ru.stresh.youamp.feature.server.create.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface StateUi {
    data class Content(
        val buttonsEnabled: Boolean,
        val returnAvailable: Boolean,
        val initWith: ServerUi? = null
    ) : StateUi

    data object Progress : StateUi
}

internal val StateUi.returnAvailable: Boolean
    get() = (this as? StateUi.Content)?.returnAvailable == true || (this is StateUi.Progress)
