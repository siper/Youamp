package ru.stersh.youamp.feature.library

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.library.data.LibraryRepositoryImpl
import ru.stersh.youamp.feature.library.domain.LibraryRepository
import ru.stersh.youamp.feature.library.ui.LibraryViewModel

val libraryModule = module {
    single<LibraryRepository> { LibraryRepositoryImpl(get(), get(), get()) }
    viewModel { LibraryViewModel(get(), get(), get(), get()) }
}