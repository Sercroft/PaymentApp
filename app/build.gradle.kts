plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.paymentappnexos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.paymentappnexos"
        minSdk = 29
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// Versions
val versionRetrofit = "2.9.0"
val versionGson = "2.9.0"
val versionRoom = "2.6.1"
val versionRoomKtx = "2.6.1"
val versionCardView = "1.0.0"

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:$versionRetrofit")
    implementation ("com.squareup.retrofit2:converter-gson:$versionGson")

    // Room
    implementation ("androidx.room:room-runtime:$versionRoom")
    implementation("androidx.room:room-ktx:$versionRoomKtx")
    kapt("androidx.room:room-compiler:$versionRoom")

    // Cardview
    implementation("androidx.cardview:cardview:$versionCardView")
}