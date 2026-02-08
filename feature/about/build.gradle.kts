plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "ru.stersh.youamp.feature.about"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        androidResources.enable = true
    }

    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:properties"))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.bundles.lifecycle)
                implementation(libs.compose.icons.simple)
                implementation(libs.compose.resources)
            }
        }
    }
}
