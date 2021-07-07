plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "com.mlamboi.weather"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // To avoid the compile error: "Cannot inline bytecode built with JVM target 1.8
    // into bytecode that is being built with JVM target 1.6"
    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
    }
}

dependencies {

    api(platform(project(":depconstraints")))

    // UI
    implementation(Libs.APPCOMPAT)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.CORE_KTX)
    implementation(Libs.MATERIAL)

    // Architecture Components
    implementation(Libs.LIFECYCLE_EXTENSION)
    implementation(Libs.LIFECYCLE_RUNTIME_KTX)
    implementation(Libs.LIFECYCLE_VIEW_MODEL_KTX)

    // Retrofit
    implementation(Libs.RETROFIT)
    implementation(Libs.RETROFIT_CONVERTER_GSON)

    // Google play services location
    implementation(Libs.PLAY_SERVICES_LOCATION)
}