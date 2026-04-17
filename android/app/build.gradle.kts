plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.luoke.location"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.luoke.location"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
}
