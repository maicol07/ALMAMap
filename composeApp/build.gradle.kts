import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
//    alias(libs.plugins.completeKotlin)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)
            dependencies {
                debugImplementation(libs.androidx.testManifest)
                implementation(libs.androidx.junit4)
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(libs.jetbrains.navigation.compose)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.composeImageLoader)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.multiplatformSettings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(compose.materialIconsExtended)
            implementation(libs.composeSettings.ui)
            implementation(libs.composeSettings.ui.extended)
            implementation(libs.composeDialogs.core)
            implementation(libs.composeDialogs.list)
            implementation(libs.compose.placeholder.material3)
            implementation(libs.koalaplot.core)
            // Kotlinx DateTime
            implementation(libs.kotlinx.datetime)
            api(libs.compose.webview.multiplatform)
            implementation(libs.ktorfit)
            implementation(libs.ktor.client.logging)
            implementation(libs.kachetor)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.appcompat)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

    }
}

android {
    namespace = "it.unibo.almamap"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
        resources.srcDirs("src/commonMain/resources")
    }
    sourceSets["debug"].apply {
        res.srcDirs("src/androidDebug/res")
    }
    //https://developer.android.com/studio/test/gradle-managed-devices
    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices.devices {
            maybeCreate<ManagedVirtualDevice>("pixel5").apply {
                device = "Pixel 5"
                apiLevel = 35
                systemImageSource = "aosp"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "values**"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            // Development purposes only
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".dev"
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}
