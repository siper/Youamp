plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget()

    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:utils"))
                implementation(libs.koin.core)
                implementation(compose.components.resources)
                implementation(libs.coil.compose)
                implementation(libs.koin.compose)
                implementation(libs.bundles.lifecycle)
                implementation(libs.compose.icons.simple)
            }
        }
    }
}

android {
    namespace = "ru.stersh.youamp.feature.settings"
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