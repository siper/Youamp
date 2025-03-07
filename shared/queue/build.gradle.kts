plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
}

kotlin {

    androidLibrary {
        namespace = "ru.stresh.youamp.shared.queue"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.core)
                implementation(libs.kotlin.coroutines.core)
                implementation(project(":core:player"))
                implementation(project(":core:api"))
            }
        }
    }
}