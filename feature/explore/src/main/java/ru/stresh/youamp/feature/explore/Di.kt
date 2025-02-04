package ru.stresh.youamp.feature.explore

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.explore.data.ExploreRepositoryImpl
import ru.stresh.youamp.feature.explore.domain.ExploreRepository
import ru.stresh.youamp.feature.explore.ui.ExploreViewModel

val exploreModule = module {
    single<ExploreRepository> { ExploreRepositoryImpl(get(), get(), get(), get()) }
    viewModel { ExploreViewModel(get(), get(), get(), get(), get()) }
}