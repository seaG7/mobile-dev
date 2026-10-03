plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "ru.mirea.danilov.anishot.data"
    compileSdk {
        version = release(37)
    }
    defaultConfig {
        minSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.appcompat)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.room.runtime)
    implementation(libs.lifecycle.livedata)
    annotationProcessor(libs.room.compiler)
}
