import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import ru.stersh.youamp.AppBuildInfo

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget()

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(libs.kermit)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.coil.compose)
            implementation(libs.bundles.lifecycle)
            implementation(libs.kotlin.serialization.core)
            implementation(libs.navigation.compose)
            implementation(libs.compose.material3.windowSizeClass)

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

android {
    namespace = "ru.stersh.youamp"
    compileSdk = libs.versions.android.compileSdk
        .get()
        .toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk
            .get()
            .toInt()
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}