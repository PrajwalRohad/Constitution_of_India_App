import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.IndiaCanon.constitutionofindia"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.IndiaCanon.constitutionofindia"
        minSdk = 27
        targetSdk = 34
        versionCode = 4
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
//            isMinifyEnabled = false
            isShrinkResources = true
            isMinifyEnabled = true
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

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-ads:23.1.0")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("androidx.fragment:fragment-ktx:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.core:core-splashscreen:1.0.1")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-ktx:1.9.0")

    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

//    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")

//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

//    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
//    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
}