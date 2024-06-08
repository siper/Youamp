package ru.stresh.youamp.core

import org.koin.dsl.module
import ru.stresh.youamp.core.properties.app.AppPropertiesStorage
import ru.stresh.youamp.core.properties.app.AppPropertiesStorageImpl

val propertiesModule = module {
    single<AppPropertiesStorage> { AppPropertiesStorageImpl(get()) }
}