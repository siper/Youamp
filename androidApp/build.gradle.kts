import ru.stersh.youamp.AppBuildInfo

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
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
        includeInApk = false
        includeInBundle = false
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

dependencies {
    implementation(project(":app"))

    implementation(libs.okhttp)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.runtime)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.resources)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.media.exoplayer)
    implementation(libs.media.session)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.guava)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.media.datasource)
    implementation(libs.media.datasource.okhttp)
    implementation(libs.kermit)

    debugImplementation(libs.compose.ui.tooling)
}
