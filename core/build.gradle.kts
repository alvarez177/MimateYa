plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.salvarez.mimateyaandroid.core"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    api(libs.kotlinx.collections.immutable)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
