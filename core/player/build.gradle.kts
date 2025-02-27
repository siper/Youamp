plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    androidLibrary {
        namespace = "ru.stresh.youamp.core.player"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.koin.core)
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
    }
}