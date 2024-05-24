plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.collegeproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.collegeproject"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    androidResources {
        noCompress(" tflite")
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("org.tensorflow:tensorflow-lite:2.4.0"){setChanging(true) }

    implementation("org.tensorflow:tensorflow-lite-gpu:2.9.0"){setChanging(true) }
    implementation("org.tensorflow:tensorflow-lite-support:0.4.0"){setChanging(true) }



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    coreLibraryDesugaring("com.android.tools.desugar_jdk_libs:2.0.2")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.journeyapps:zxing-android-embedded:4.1.0")
    implementation ("com.google.firebase:firebase-analytics:17.4.1")
    implementation("com.google.zxing:core:3.3.3")
    implementation ("androidx.camera:camera-core:1.0.0-rc01")
    implementation ("androidx.camera:camera-camera2:1.0.0-rc01")
    implementation ("androidx.camera:camera-lifecycle:1.0.0-rc01")
    implementation ("androidx.camera:camera-view:1.0.0-alpha29")
    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    implementation("com.google.mlkit:face-detection:16.1.5")

    implementation("com.google.android.gms:play-services-location:21.0.1")





}