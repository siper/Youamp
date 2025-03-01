plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:properties"))
                implementation(libs.koin.core)
                implementation(libs.koin.compose.kmp)
                implementation(libs.lifecycle.viewmodel)
                implementation(libs.compose.icons.simple)
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "ru.stersh.youamp.feature.about"
    defaultConfig {
        compileSdk = libs.versions.android.compileSdk
            .get()
            .toInt()
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