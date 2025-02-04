package ru.stersh.youamp.feature.personal

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.personal.data.PersonalRepositoryImpl
import ru.stersh.youamp.feature.personal.domain.PersonalRepository
import ru.stersh.youamp.feature.personal.ui.PersonalViewModel

val personalModule = module {
    single<PersonalRepository> { PersonalRepositoryImpl(get(), get(), get(), get(), get(), get()) }
    viewModel { PersonalViewModel(get(), get(), get(), get(), get()) }
}