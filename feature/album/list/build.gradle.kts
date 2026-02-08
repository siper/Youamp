plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "ru.stersh.youamp.feature.album.list"
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
                implementation(project(":core:api"))
                implementation(project(":core:utils"))
                implementation(libs.koin.core)
                implementation(libs.coil.compose)
                implementation(libs.koin.compose)
                implementation(libs.bundles.lifecycle)
                implementation(libs.compose.resources)
                implementation(libs.kotlinx.collectionsImmutable)
                implementation(libs.kermit)
            }
        }
    }
}
