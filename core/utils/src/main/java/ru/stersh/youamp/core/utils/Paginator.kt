package ru.stersh.youamp.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun <D> pageLoader(
    startPage: Int = 1,
    pageSize: Int = 20,
    onLoadData: suspend (page: Int, pageSize: Int) -> List<D>
): Paginator<List<D>, Int> {
    var hasNextPage = true

    return Paginator(
        startIdentifier = startPage,
        onLoadData = { page ->
            val data = onLoadData(page, pageSize)
            hasNextPage = data.size == pageSize
            data
        },
        onNewIdentifier = { counter, _ ->
            counter.inc().takeIf { hasNextPage }
        }
    )
}

fun <D, I> Paginator<List<D>, I>.data(): Flow<List<D>> {
    return pages()
        .map { pages ->
            pages
                .filterIsInstance<Content<List<D>, I>>()
                .map { it.content }
                .flatten()
        }
}

fun <I> Paginator<*, I>.isRefreshing(): Flow<Boolean> {
    return pages()
        .map { pages ->
            pages.any { it is Progress<I> }
        }
}

class Paginator<D, I>(
    private val startIdentifier: I,
    private val onLoadData: suspend (identifier: I) -> D,
    private val onNewIdentifier: suspend (currentIdentifier: I, params: Any?) -> I?
) {
    private val mutex = Mutex()
    private val pages = MutableStateFlow<List<Page<I>>>(listOf())
    private val state = MutableStateFlow(State.Idle)

    fun pages(): Flow<List<Page<I>>> = pages

    fun state(): Flow<State> = state

    suspend fun restart() = mutex.withLock {
        pages.value = emptyList()
        state.value = State.Restart
        loadPage(startIdentifier)
        state.value = State.Idle
    }

    suspend fun loadNextPage(params: Any? = null) = mutex.withLock {
        val currentIdentifier = pages.value.lastOrNull()?.identifier
        val identifier = if (currentIdentifier == null) {
            startIdentifier
        } else {
            onNewIdentifier(currentIdentifier, params)
        } ?: return@withLock
        state.value = State.LoadPage
        loadPage(identifier)
        state.value = State.Idle
    }

    private suspend fun loadPage(identifier: I) {
        updatePage(Progress(identifier))
        runCatching { onLoadData.invoke(identifier) }.fold(
            onSuccess = {
                updatePage(Content(identifier, it))
            },
            onFailure = {
                updatePage(Error(identifier, it))
            }
        )
    }

    private fun updatePage(page: Page<I>) {
        val pagesData = pages.value.toMutableList()
        pagesData.removeAll { it.identifier == page.identifier }
        pagesData.add(page)
        pages.value = pagesData.toList()
    }

    enum class State { Idle, Restart, LoadPage }
}

sealed interface Page<I> {
    val identifier: I
}

data class Content<D, I>(
    override val identifier: I,
    val content: D
) : Page<I>

data class Progress<I>(
    override val identifier: I
) : Page<I>

data class Error<I>(
    override val identifier: I,
    val throwable: Throwable
) : Page<I>