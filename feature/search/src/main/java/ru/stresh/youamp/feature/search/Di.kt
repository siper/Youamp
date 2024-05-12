package ru.stresh.youamp.feature.search

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.search.data.SearchRepositoryImpl
import ru.stresh.youamp.feature.search.domain.SearchRepository
import ru.stresh.youamp.feature.search.ui.SearchViewModel

val searchModule = module {
    factory<SearchRepository> { SearchRepositoryImpl(get()) }
    viewModel { SearchViewModel(get(), get()) }
}