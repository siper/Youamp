plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
    alias(libs.plugins.osdetector)
}

kotlin {
    androidLibrary {
        namespace = "ru.stresh.youamp.core.player"
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

    sourceSets {

        commonMain {
            dependencies {
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.koin.core)
                implementation(project(":core:utils"))
            }
        }

        androidMain {
            dependencies {
                implementation(libs.kotlin.coroutines.android)
                implementation(libs.media.exoplayer)
                implementation(libs.media.exoplayer.hls)
                implementation(libs.media.datasource)
                implementation(libs.media.datasource.okhttp)
                implementation(libs.media.session)
                implementation(libs.media.extractor)
                implementation(libs.kotlin.coroutines.guava)
                implementation(libs.androidx.core)
                implementation(libs.koin.android)
            }
        }

        val desktopMain by getting {
            dependencies {
                val javafxVersion = "21.0.6"
                val osSuffix =
                    when (osdetector.classifier) {
                        "linux-x86_64" -> "linux"
                        "linux-aarch_64" -> "linux-aarch64"
                        "windows-x86_64" -> "win"
                        "osx-x86_64" -> "mac"
                        "osx-aarch_64" -> "mac-aarch64"
                        else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
                    }
                implementation("org.openjfx:javafx-base:$javafxVersion:$osSuffix")
                implementation("org.openjfx:javafx-media:$javafxVersion:$osSuffix")
                implementation("org.openjfx:javafx-swing:$javafxVersion:$osSuffix")
                implementation("org.openjfx:javafx-web:$javafxVersion:$osSuffix")
                implementation("org.openjfx:javafx-controls:$javafxVersion:$osSuffix")
                implementation("org.openjfx:javafx-graphics:$javafxVersion:$osSuffix")
            }
        }
    }
}
