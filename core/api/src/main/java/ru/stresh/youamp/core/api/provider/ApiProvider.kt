package ru.stresh.youamp.core.api.provider

import ru.stresh.youamp.core.api.SubsonicApi

interface ApiProvider {
    suspend fun getApi(): SubsonicApi
}
