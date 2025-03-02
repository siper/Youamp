package ru.stersh.youamp.feature.search

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.search.data.SearchRepositoryImpl
import ru.stersh.youamp.feature.search.domain.SearchRepository
import ru.stersh.youamp.feature.search.ui.SearchViewModel

val searchModule = module {
    factory<SearchRepository> { SearchRepositoryImpl(get()) }
    viewModel { SearchViewModel(get(), get()) }
}