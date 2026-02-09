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
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.material3AdaptiveNavigationSuite)
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
            implementation(libs.compose.resources)
            implementation(libs.kotlin.coroutines.swing)
            implementation(libs.coil.network.okhttp)
            implementation(project.dependencies.platform(libs.okhttp.bom))
            implementation(libs.okhttp)
            implementation(libs.okhttp.sse)
            implementation(libs.sl4j)
            implementation(libs.sl4j.simple)
        }
    }
}

compose.desktop {
    application {
        mainClass = "ru.stersh.youamp.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb,
            )
            packageName = "Youamp"
            packageVersion = AppBuildInfo.CLEAR_VERSION_NAME
        }
        buildTypes.release.proguard {
            isEnabled = false
        }
    }
}
