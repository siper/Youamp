plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
    alias(libs.plugins.osdetector)
}

kotlin {
    androidLibrary {
        namespace = "ru.stresh.youamp.core.player"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
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
                val fxSuffix = when (osdetector.classifier) {
                    "linux-x86_64" -> "linux"
                    "linux-aarch_64" -> "linux-aarch64"
                    "windows-x86_64" -> "win"
                    "osx-x86_64" -> "mac"
                    "osx-aarch_64" -> "mac-aarch64"
                    else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
                }
                implementation("org.openjfx:javafx-base:21.0.6:${fxSuffix}")
                implementation("org.openjfx:javafx-media:21.0.6:${fxSuffix}")
                implementation("org.openjfx:javafx-swing:21.0.6:${fxSuffix}")
                implementation("org.openjfx:javafx-web:21.0.6:${fxSuffix}")
                implementation("org.openjfx:javafx-controls:21.0.6:${fxSuffix}")
                implementation("org.openjfx:javafx-graphics:21.0.6:${fxSuffix}")
            }
        }
    }
}