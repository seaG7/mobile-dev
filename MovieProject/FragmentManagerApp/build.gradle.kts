plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.mirea.danilov.fragmentmanagerapp"
    compileSdk {
        version = release(37)
    }
    defaultConfig {
        applicationId = "ru.mirea.danilov.fragmentmanagerapp"
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
    implementation(libs.fragment)
    implementation(libs.recyclerview)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
}
