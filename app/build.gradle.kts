plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android") version "2.51.1" apply true
    id("com.google.gms.google-services")
}
hilt {
    enableAggregatingTask = false
}

android {
    namespace = "com.ayub.khosa.chatapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ayub.khosa.chatapplication"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["auth0Domain"] = "https://oauth2.googleapis.com/"
        manifestPlaceholders["auth0Scheme"] = "https"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            resources.excludes.add("META-INF/*")

        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.googleid)
    implementation(libs.volley)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // material.icons.filled.Visibility
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

//    Navigation with Compose
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
// LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-messaging:24.0.0") // Check for the latest version
    implementation("com.google.firebase:firebase-database")

    // Declare the dependency for the Cloud Firestore library
    implementation("com.google.firebase:firebase-firestore")
    // Add the main storage library dependency
    implementation("com.google.firebase:firebase-storage")
    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("androidx.room:room-compiler:2.6.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")


    // notification permission
    implementation("com.google.accompanist:accompanist-permissions:0.31.1-alpha")

//    Inject ViewModel objects with Hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.squareup.okhttp3:okhttp:4.7.2")

    //Authentication with Credential Manager
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.google.android.gms:play-services-gcm:17.0.0")
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    implementation("com.auth0.android:auth0:2.5.1")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.8.0")



    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1") // Check for the latest version

    implementation("io.coil-kt:coil-compose:2.0.0-rc01")
}
