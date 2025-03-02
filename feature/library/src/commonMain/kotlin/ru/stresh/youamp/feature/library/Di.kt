package ru.stresh.youamp.feature.library

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.library.data.LibraryRepositoryImpl
import ru.stresh.youamp.feature.library.domain.LibraryRepository
import ru.stresh.youamp.feature.library.ui.LibraryViewModel

val libraryModule = module {
    single<LibraryRepository> { LibraryRepositoryImpl(get(), get(), get()) }
    viewModel { LibraryViewModel(get(), get(), get(), get()) }
}