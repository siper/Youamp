package ru.stersh.youamp.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <C: Iterable<T>, T, R> Flow<C>.mapItems(crossinline block: (item: T) -> R): Flow<List<R>> {
    return map { collection ->
        collection.map { block(it) }
    }
}