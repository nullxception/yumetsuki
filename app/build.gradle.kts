@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.mikepenz.aboutlibraries.plugin.DuplicateMode
import com.mikepenz.aboutlibraries.plugin.DuplicateRule
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

val keystorePropertiesFile = rootProject.file("key.properties")
val keystoreProperties = Properties()

keystoreProperties.load(FileInputStream(keystorePropertiesFile))

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("com.mikepenz.aboutlibraries.plugin")
    id("com.ncorti.ktfmt.gradle") version "0.12.0"
}

ktfmt { kotlinLangStyle() }

android {
    compileSdk = 34
    namespace = "io.chaldeaprjkt.yumetsuki"

    defaultConfig {
        minSdk = 29
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 32 // use 32 since permission handling somehow isn't working well
        versionCode = 26
        versionName = "2.6"
        vectorDrawables { useSupportLibrary = true }

        ksp { arg("room.schemaLocation", "$projectDir/schemas") }
    }

    signingConfigs {
        create("config") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("config")
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("config")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all {
        val versionType = buildType.name
        outputs.all {
            (this as? ApkVariantOutputImpl)?.apply {
                outputFileName = "Yumetsuki-v$versionName-${versionType}.apk"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions { jvmTarget = "11" }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }

    kapt { correctErrorTypes = true }
    lint {
        disable += "SmallSp" // used on several widget text
    }
}

dependencies {
    val versionCompose = "1.6.8"
    val versionAccompanist = "0.31.3-beta"
    val versionHilt = "2.49"
    val versionRoom = "2.6.1"
    val versionMoshi = "1.15.1"

    // Android
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material:material-icons-extended:$versionCompose")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui:$versionCompose")
    implementation("androidx.compose.ui:ui-tooling-preview:$versionCompose")
    implementation("androidx.compose.ui:ui-viewbinding:$versionCompose")
    implementation("androidx.datastore:datastore:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.compose.foundation:foundation:1.6.8")
    implementation("com.google.accompanist:accompanist-navigation-material:$versionAccompanist")
    implementation("com.google.accompanist:accompanist-permissions:$versionAccompanist")
    implementation("com.google.accompanist:accompanist-navigation-animation:$versionAccompanist")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    debugImplementation("androidx.compose.ui:ui-tooling:$versionCompose")
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")

    // Dependency Injection
    implementation("com.google.dagger:hilt-android:$versionHilt")
    implementation("androidx.hilt:hilt-work:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-android-compiler:$versionHilt")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    implementation("com.github.skydoves:sandwich:1.3.7")

    // Storage
    implementation("androidx.room:room-runtime:$versionRoom")
    implementation("androidx.room:room-ktx:$versionRoom")
    ksp("androidx.room:room-compiler:$versionRoom")

    // Parser
    implementation("com.squareup.moshi:moshi:$versionMoshi")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$versionMoshi")

    // Assets
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("br.com.devsrsouza.compose.icons.android:feather:1.0.0")

    // Licenses
    implementation("com.mikepenz:aboutlibraries-core:10.7.0")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.freeCompilerArgs +=
            listOf(
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
                "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi"
            )
    }
}

aboutLibraries {
    duplicationMode = DuplicateMode.LINK
    duplicationRule = DuplicateRule.SIMPLE
}
