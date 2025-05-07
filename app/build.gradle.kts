plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.medcare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.medcare"
        minSdk = 25
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.firebase.bom)
    implementation (libs.firebase.auth)
    implementation (libs.firebase.database.v2003)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    implementation("com.mikepenz:iconics-core:5.3.0")
    implementation("com.mikepenz:iconics-views:5.3.0")
    implementation("com.mikepenz:fontawesome-typeface:5.13.3.1-kotlin@aar")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("org.osmdroid:osmdroid-android:6.1.16")
    implementation ("com.cloudinary:cloudinary-android:2.3.1")
    implementation ("androidx.activity:activity:1.6.1")



}