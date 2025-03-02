package ru.stersh.youamp

import android.content.Context
import androidx.room.RoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.stersh.youamp.audio.auto.AutoRepository
import ru.stersh.youamp.audio.auto.AutoRepositoryImpl
import ru.stresh.youamp.core.db.Database
import ru.stresh.youamp.core.db.getDatabaseBuilder
import ru.stresh.youamp.core.properties.app.AppProperties

val androidModule: Module = module {
    single<AutoRepository> { AutoRepositoryImpl(get()) }
    single {
        AppProperties(
            name = get<Context>().getString(R.string.app_name),
            version = BuildConfig.VERSION_NAME,
            githubUri = "https://github.com/siper/Youamp",
            fdroidUri = "https://f-droid.org/packages/ru.stersh.youamp/",
            crwodinUri = "https://crowdin.com/project/youamp"
        )
    }
    single<RoomDatabase.Builder<Database>> { getDatabaseBuilder(get()) }
}