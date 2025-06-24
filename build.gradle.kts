// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // 👇 THÊM Hilt plugin classpath
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.56.2")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}