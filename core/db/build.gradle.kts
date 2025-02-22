plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ksp)
}

kotlin {
    androidLibrary {
        namespace = "ru.stresh.youamp.db"
        compileSdk = 35
        minSdk = 24
    }

    val xcfName = "dbKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }
    jvm {
        withJava()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.room.core)
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.koin.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }

}

dependencies {
    add("kspCommonMainMetadata", libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
}