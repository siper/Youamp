import com.android.build.api.dsl.androidLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    androidLibrary {
        namespace = "ru.stersh.youamp.feature.album.favorites"
        compileSdk = libs.versions.android.compileSdk
            .get()
            .toInt()
        minSdk = libs.versions.android.minSdk
            .get()
            .toInt()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:properties"))
                implementation(project(":shared:queue"))
                implementation(project(":shared:favorites"))
                implementation(libs.koin.core)
                implementation(libs.coil.compose)
                implementation(libs.koin.compose)
                implementation(libs.lifecycle.viewmodel)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.collectionsImmutable)
                implementation(libs.kermit)
            }
        }
    }
}