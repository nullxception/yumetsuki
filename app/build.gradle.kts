@file:Suppress("UnstableApiUsage")

import android.annotation.SuppressLint
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.mikepenz.aboutlibraries.plugin.DuplicateMode
import com.mikepenz.aboutlibraries.plugin.DuplicateRule
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    compileSdk = 33
    namespace = "io.chaldeaprjkt.yumetsuki"

    defaultConfig {
        minSdk = 29
        @SuppressLint("OldTargetApi") // TODO: Fix WallpaperManager usage on targetSdk 33
        targetSdk = 32
        versionCode = 4
        versionName = "1.3"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    signingConfigs {
        create("platform") {
            keyAlias = "android"
            keyPassword = "android"
            storeFile = file("plat.aosp.jks")
            storePassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("platform")
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("platform")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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

    kotlinOptions {
        jvmTarget = "11"
    }

    sourceSets {
        findByName("main")?.java?.srcDirs(project.file("src/main/kotlin"))
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    kapt {
        correctErrorTypes = true
    }
    lint {
        disable += "SmallSp" // used on several widget text
    }
}

dependencies {
    // Android
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.material:material-icons-extended:1.4.0-alpha01")
    implementation("androidx.compose.material3:material3:1.1.0-alpha01")
    implementation("androidx.compose.ui:ui:1.4.0-alpha01")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0-alpha01")
    implementation("androidx.compose.ui:ui-viewbinding:1.4.0-alpha01")
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    implementation("com.google.accompanist:accompanist-flowlayout:0.26.5-rc")
    implementation("com.google.accompanist:accompanist-navigation-material:0.26.5-rc")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.26.5-rc")
    implementation("com.google.accompanist:accompanist-pager:0.26.5-rc")
    implementation("com.google.accompanist:accompanist-permissions:0.26.5-rc")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.26.5-rc")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.0-alpha01")

    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.10")
    implementation("com.github.skydoves:sandwich:1.3.2")

    // Storage
    val versionRoom = "2.4.3"
    implementation("androidx.room:room-runtime:$versionRoom")
    implementation("androidx.room:room-ktx:$versionRoom")
    ksp("androidx.room:room-compiler:$versionRoom")

    // Parser
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    // Assets
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("br.com.devsrsouza.compose.icons.android:feather:1.0.0")

    // Licenses
    implementation("com.mikepenz:aboutlibraries-core:10.5.1")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
            "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi",
            "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
        )
    }
}

aboutLibraries {
    duplicationMode = DuplicateMode.LINK
    duplicationRule = DuplicateRule.SIMPLE
}
