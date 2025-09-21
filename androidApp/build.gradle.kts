import ru.stersh.youamp.AppBuildInfo

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.material)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.media.exoplayer)
            implementation(libs.media.session)
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.kotlin.coroutines.guava)
            implementation(libs.kotlin.coroutines.android)
            implementation(libs.coil.network.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.koin.core)
            implementation(libs.coil.compose)
            implementation(libs.kermit)

            implementation(project(":app"))
        }
    }
}

android {
    namespace = AppBuildInfo.PACKAGE_NAME
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = AppBuildInfo.PACKAGE_NAME
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = AppBuildInfo.VERSION_CODE
        versionName = AppBuildInfo.VERSION_NAME
        base.archivesName = "Youamp-$versionName"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
