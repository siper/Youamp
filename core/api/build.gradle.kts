plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "ru.stresh.youamp.core.api"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "apiKit"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.subsonicApi)
                implementation(libs.kotlin.coroutines.core)
            }
        }
    }
}
