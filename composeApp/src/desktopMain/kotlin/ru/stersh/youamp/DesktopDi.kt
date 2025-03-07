package ru.stersh.youamp

import androidx.room.RoomDatabase
import org.koin.dsl.module
import ru.stersh.youamp.core.db.Database
import ru.stersh.youamp.core.db.getDatabaseBuilder
import ru.stersh.youamp.core.properties.app.AppProperties

val desktopModule = module {
    single {
        AppProperties(
            name = "Youamp",
            version = "2.0.0-beta06",
            githubUrl = "https://github.com/siper/Youamp",
            fdroidUrl = "https://f-droid.org/packages/ru.stersh.youamp/",
            crwodinUrl = "https://crowdin.com/project/youamp"
        )
    }
    single<RoomDatabase.Builder<Database>> { getDatabaseBuilder() }
}