plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.globant.ticketmaster"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.globant.ticketmaster"
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        buildConfig = true
        compose = true
    }

    testOptions {
        packaging {
            resources.excludes.add("META-INF/*")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.timber)
    implementation(libs.kotlinx.serialization.json)

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:models:ui"))
    implementation(project(":core:models:domain"))

    implementation(project(":feature:events"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:detailevent"))
    implementation(project(":feature:searchevent"))
    implementation(project(":feature:lastvisited"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:countries"))
}
