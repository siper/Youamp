package ru.stresh.youamp.core.properties.app

internal class AppPropertiesStorageImpl(
    private val appProperties: AppProperties
) : AppPropertiesStorage {

    override fun getAppProperties(): AppProperties {
        return appProperties
    }
}