package ru.stresh.youamp.feature.library.domain

import kotlinx.coroutines.flow.Flow

internal interface LibraryRepository {
    fun getLibrary(): Flow<Library>
}