package ru.stresh.youamp.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun <D> pageLoader(
    startPage: Int = 0,
    pageSize: Int = 20,
    onLoadData: suspend (page: Int, pageSize: Int) -> List<D>
): Paginator<List<D>, Int> {
    var hasNextPage = true

    return Paginator(
        startCounter = startPage,
        onLoadData = {
            val data = onLoadData(it, pageSize)
            hasNextPage = data.size >= pageSize
            data
        },
        onNewIdentifier = { it.takeIf { hasNextPage } }
    )
}

fun <D, I> Paginator<List<D>, I>.data(): Flow<List<D>> {
    return pages()
        .map { pages ->
            pages
                .filterIsInstance<Content<List<D>, I>>()
                .flatMap { it.content }
        }
}

fun <I> Paginator<*, I>.isRefreshing(): Flow<Boolean> {
    return pages()
        .map { pages ->
            pages.any { it is Progress<I> }
        }
}

class Paginator<D, I>(
    private val startCounter: Int = 0,
    private val onLoadData: suspend (identifier: I) -> D,
    private val onNewIdentifier: suspend (counter: Int) -> I?
) {
    private val mutex = Mutex()
    private var currentCounter = startCounter
    private val pages = MutableStateFlow<List<Page<I>>>(listOf())
    private val state = MutableStateFlow(State.Idle)

    fun pages(): Flow<List<Page<I>>> = pages

    fun state(): Flow<State> = state

    suspend fun restart() = mutex.withLock {
        currentCounter = startCounter
        val identifier = onNewIdentifier(currentCounter) ?: return@withLock
        state.value = State.Restart
        loadPage(identifier)
        state.value = State.Idle
    }

    suspend fun loadNextPage() = mutex.withLock {
        val identifier = onNewIdentifier(currentCounter) ?: return@withLock
        currentCounter++
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