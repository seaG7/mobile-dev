plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.mirea.danilov.retrofitapp"
    compileSdk {
        version = release(37)
    }
    defaultConfig {
        applicationId = "ru.mirea.danilov.retrofitapp"
        minSdk = 33
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.recyclerview)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.picasso)
}
