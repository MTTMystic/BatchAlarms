// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.compose.compiler)
    id("com.google.protobuf") version "0.9.5" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.20" apply false
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
}
buildscript{
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.9.5")
    }
}
