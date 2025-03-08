package ru.stersh.youamp.feature.about.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.stersh.youamp.core.properties.app.AppProperties
import ru.stersh.youamp.core.properties.app.AppPropertiesStorage

internal class AboutAppViewModel(appPropertiesStorage: AppPropertiesStorage) : ViewModel() {
    private val _state = MutableStateFlow(appPropertiesStorage.getAppProperties().toState())

    val state: StateFlow<AboutStateUi>
        get() = _state

    private fun AppProperties.toState(): AboutStateUi {
        return AboutStateUi(
            name = name,
            version = version,
            githubUri = githubUrl,
            fdroidUri = fdroidUrl,
            crwodinUri = crwodinUrl
        )
    }
}