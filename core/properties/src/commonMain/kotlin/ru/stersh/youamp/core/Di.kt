package ru.stersh.youamp.core

import org.koin.dsl.module
import ru.stersh.youamp.core.properties.app.AppPropertiesStorage
import ru.stersh.youamp.core.properties.app.AppPropertiesStorageImpl

val propertiesModule = module {
    single<AppPropertiesStorage> { AppPropertiesStorageImpl(get()) }
}