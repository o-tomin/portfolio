// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    // https://mvnrepository.com/artifact/com.google.dagger/hilt-android
    id("com.google.dagger.hilt.android") version "2.52" apply false
}