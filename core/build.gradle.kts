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
    api(libs.kotlinx.coroutines.core)
    api(libs.javax.inject)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
