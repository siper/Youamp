plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "ru.stersh.youamp.app"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        androidResources.enable = true
    }

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.material3AdaptiveNavigationSuite)
            implementation(libs.kermit)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.coil.compose)
            implementation(libs.bundles.lifecycle)
            implementation(libs.kotlin.serialization.core)
            implementation(libs.navigation.compose)

            api(project(":core:api"))
            api(project(":core:ui"))
            api(project(":core:db"))
            api(project(":core:utils"))
            api(project(":core:properties"))
            api(project(":core:player"))
            api(project(":shared:queue"))
            api(project(":shared:favorites"))
            api(project(":shared:song:random"))
            api(project(":feature:album:list"))
            api(project(":feature:album:info"))
            api(project(":feature:album:favorites"))
            api(project(":feature:artist:list"))
            api(project(":feature:artist:info"))
            api(project(":feature:artist:favorites"))
            api(project(":feature:playlist:list"))
            api(project(":feature:playlist:info"))
            api(project(":feature:main"))
            api(project(":feature:player:mini"))
            api(project(":feature:player:screen"))
            api(project(":feature:player:queue"))
            api(project(":feature:server:create"))
            api(project(":feature:server:list"))
            api(project(":feature:search"))
            api(project(":feature:song:info"))
            api(project(":feature:song:favorites"))
            api(project(":feature:settings"))
            api(project(":feature:about"))
            api(project(":feature:personal"))
            api(project(":feature:explore"))
            api(project(":feature:library"))
            api(project(":feature:song:random"))
        }
    }
}
