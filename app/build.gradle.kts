import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.smartday"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.smartday.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 4
        versionName = "1.3.3-beta"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")


        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            javaParameters.set(true)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(path = ":data"))
    implementation(project(path = ":domain"))
    implementation(project(path = ":core"))

    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    androidTestImplementation(libs.androidx.navigation.testing)

    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    ksp(libs.androidx.room.compiler)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.workmanager)
    testImplementation(libs.koin.test)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}