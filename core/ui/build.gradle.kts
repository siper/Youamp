plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "ru.stersh.youamp.core.ui"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        androidResources.enable = true
    }

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3.material3)
            api(libs.compose.material.materialIconsExtended)
            api(libs.compose.ui.tooling)
            api(libs.compose.ui.tooling.preview)
            api(libs.compose.material3.windowSizeClass)
            api(libs.coil.compose)
            implementation(libs.compose.resources)
            implementation(libs.kotlinx.collectionsImmutable)
        }
    }
}
