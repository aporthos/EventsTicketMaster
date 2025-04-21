plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.globant.ticketmaster.core.data"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    kotlinOptions.freeCompilerArgs +=
        listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=androidx.paging.ExperimentalPagingApi",
        )
}

dependencies {

    implementation(libs.androidx.core.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.moshi)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.okhttp3.interceptor)
    implementation(libs.squareup.moshi)
    implementation(libs.squareup.moshi.kotlin)
    kapt(libs.squareup.moshi.codegen)

    implementation(libs.timber)
    implementation(libs.androidx.paging.runtime)

    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:models:domain"))
    implementation(project(":core:models:entity"))
    implementation(project(":core:models:network"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))
}
