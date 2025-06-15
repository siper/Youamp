package ru.stersh.youamp.core.properties.app

internal class AppPropertiesStorageImpl(
    private val appProperties: AppProperties,
) : AppPropertiesStorage {
    override fun getAppProperties(): AppProperties = appProperties
}
