plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlinx-serialization")
//    alias(libs.plugins.jetbrains.kotlin.serialization)
    id("com.google.gms.google-services")
    kotlin("kapt") // necesario para procesar anotaciones
    //id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.anizeno"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.anizeno"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        target {
            compilerOptions {
                optIn.add("kotlin.RequiresOptIn")
            }
        }
    }
    buildFeatures {
        compose = true
    }

}
//noinspection UseTomlInstead //disable format warning
dependencies {
    implementation("com.google.dagger:hilt-android:2.57.2")
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3.window.size.class1)
    ksp("com.google.dagger:hilt-android-compiler:2.57.2")
    implementation("com.google.dagger:hilt-android:2.57.2")
    kapt("com.google.dagger:hilt-compiler:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")


    /*Room*/
    dependencies {
        // formatos de pantalla adaptados(igual no es necesario)
        implementation("androidx.compose.material3:material3-window-size-class:1.2.0")

        val room_version = "2.8.3"

        implementation("androidx.room:room-runtime:$room_version")

        // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
        // See Add the KSP plugin to your project
        ksp("androidx.room:room-compiler:$room_version")

        // If this project only uses Java source, use the Java annotationProcessor
        // No additional plugins are necessary
        annotationProcessor("androidx.room:room-compiler:$room_version")

        // optional - Kotlin Extensions and Coroutines support for Room
        implementation("androidx.room:room-ktx:$room_version")

        // optional - RxJava2 support for Room
        implementation("androidx.room:room-rxjava2:$room_version")

        // optional - RxJava3 support for Room
        implementation("androidx.room:room-rxjava3:$room_version")

        // optional - Guava support for Room, including Optional and ListenableFuture
        implementation("androidx.room:room-guava:$room_version")

        // optional - Test helpers
        testImplementation("androidx.room:room-testing:$room_version")

        // optional - Paging 3 Integration
        implementation("androidx.room:room-paging:$room_version")
    }
    /*Firebase*/
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    /*retrofit*/
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")

    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation(libs.androidx.material.icons.extended)
    //navigation
    implementation(libs.androidx.navigation.compose)
    //navigation3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    //core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation(libs.material3)
    //testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}