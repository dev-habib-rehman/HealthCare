import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.navigation.safeargs.kotlin)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.devtools.ksp)
}

val gradleProperties = Properties().apply {
    load(project.rootProject.file("gradle.properties").inputStream())
}

android {
    namespace = "com.example.health"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.health"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {

        debug {
            buildConfigField("String", "BASE_URL", "\"https://run.mocky.io/\"")
        }

        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"https://run.mocky.io/\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    androidTestImplementation( "androidx.test:core:1.5.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.org.mockito.core)
    testImplementation(libs.androidx.arch.core)
    testImplementation(libs.org.jetbrains.kotlinx.test)
    testImplementation(libs.dagger.hilt.android.testing)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.app.cash.turbine)

    //navigation
    implementation(libs.androidx.navigation.fragemnt)
    implementation(libs.androidx.navigation.compose)

    // Hilt Dependency
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.extension)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

}