import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import ru.stersh.youamp.AppBuildInfo

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(libs.kermit)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.coil.compose)
            implementation(libs.bundles.lifecycle)
            implementation(libs.kotlin.serialization.core)
            implementation(libs.navigation.compose)

            implementation(project(":app"))
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.components.resources)
            implementation(libs.kotlin.coroutines.swing)
            implementation(libs.coil.network.okhttp)
            implementation(libs.sl4j)
            implementation(libs.sl4j.simple)
        }
    }
}

compose.desktop {
    application {
        mainClass = "ru.stersh.youamp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Youamp"
            packageVersion = AppBuildInfo.CLEAR_VERSION_NAME
        }
        buildTypes.release.proguard {
            isEnabled = false
        }
    }
}
