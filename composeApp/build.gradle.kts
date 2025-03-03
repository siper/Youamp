plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget()

//    jvm("desktop")

    sourceSets {
//        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.media.exoplayer)
            implementation(libs.media.session)
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.kotlin.coroutines.guava)
            implementation(libs.kotlin.coroutines.android)
            implementation(libs.coil.network.okhttp)
            implementation(libs.coil.network.okhttp)
            implementation(libs.material)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kermit)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.coil.compose)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.bundles.lifecycle)
            implementation(libs.kotlin.serialization.core)
            implementation(libs.navigation.compose)

            implementation(project(":core:api"))
            implementation(project(":core:ui"))
            implementation(project(":core:db"))
            implementation(project(":core:utils"))
            implementation(project(":core:properties"))
            implementation(project(":core:player"))
            implementation(project(":shared:queue"))
            implementation(project(":shared:favorites"))
            implementation(project(":shared:song:random"))
            implementation(project(":feature:album:list"))
            implementation(project(":feature:album:info"))
            implementation(project(":feature:album:favorites"))
            implementation(project(":feature:artist:list"))
            implementation(project(":feature:artist:info"))
            implementation(project(":feature:artist:favorites"))
            implementation(project(":feature:playlist:list"))
            implementation(project(":feature:playlist:info"))
            implementation(project(":feature:main"))
            implementation(project(":feature:player:mini"))
            implementation(project(":feature:player:screen"))
            implementation(project(":feature:player:queue"))
            implementation(project(":feature:server:create"))
            implementation(project(":feature:server:list"))
            implementation(project(":feature:search"))
            implementation(project(":feature:song:info"))
            implementation(project(":feature:song:favorites"))
            implementation(project(":feature:settings"))
            implementation(project(":feature:about"))
            implementation(project(":feature:personal"))
            implementation(project(":feature:explore"))
            implementation(project(":feature:library"))
            implementation(project(":feature:song:random"))
        }

//        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//        }
    }
}

android {
    namespace = "ru.stersh.youamp"
    compileSdk = libs.versions.android.compileSdk
        .get()
        .toInt()

    defaultConfig {
        applicationId = "ru.stersh.youamp"
        minSdk = libs.versions.android.minSdk
            .get()
            .toInt()
        targetSdk = libs.versions.android.targetSdk
            .get()
            .toInt()
        versionCode = 23
        versionName = "2.0.0-beta06"
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
    sourceSets["main"].res.srcDirs(
        "src/commonMain/resources",
        "src/androidMain/resources"
    )
}

dependencies {
    debugImplementation(compose.uiTooling)
}

//compose.desktop {
//    application {
//        mainClass = "ru.stersh.youamp.MainKt"
//
//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "ru.stersh.youamp"
//            packageVersion = "1.0.0"
//        }
//    }
//}
