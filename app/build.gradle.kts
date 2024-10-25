plugins {
    //alias(libs.plugins.androidApplication)

    id("com.android.application")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.g2_movieapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.g2_movieapp"
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
}

dependencies {

    implementation(libs.activity)
    //    implementation(libs.activity)
    val ext = object {
        val roomVersion = "2.6.1" // Update this to the latest Room version
        val appCompatVersion = "1.6.1" // Update this to the latest AppCompat version
        val constraintLayoutVersion = "2.1.4" // Update this to the latest ConstraintLayout version
        val materialVersion = "1.8.0" // Update this to the latest Material Design version
        val lifecycleVersion = "2.5.1" // Update this to the latest Lifecycle version
        val coreTestingVersion = "2.1.0" // Update this to the latest Core Testing version
        val junitVersion = "4.13.2" // Update this to the latest JUnit version
        val espressoVersion = "3.5.1" // Update this to the latest Espresso version
        val androidxJunitVersion = "1.1.5" // Update this to the latest AndroidX JUnit version
    }

    implementation("androidx.appcompat:appcompat:${ext.appCompatVersion}")
    implementation("com.google.android.material:material:1.4.0")


    // Dependencies for working with Architecture components
    // You'll probably have to update the version numbers in build.gradle (Project)

    //Room components
    implementation("androidx.room:room-runtime:${ext.roomVersion}")
    annotationProcessor("androidx.room:room-compiler:${ext.roomVersion}")
    androidTestImplementation("androidx.room:room-testing:${ext.roomVersion}")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel:${ext.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata:${ext.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${ext.lifecycleVersion}")

    // UI
    implementation("androidx.constraintlayout:constraintlayout:${ext.constraintLayoutVersion}")
    implementation("com.google.android.material:material:${ext.materialVersion}")

    // Testing
    testImplementation("junit:junit:${ext.junitVersion}")
    androidTestImplementation("androidx.arch.core:core-testing:${ext.coreTestingVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${ext.espressoVersion}", {
        exclude(group = "com.android.support", module = "support-annotations")
    })
    androidTestImplementation("androidx.test.ext:junit:${ext.androidxJunitVersion}")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.android.volley:volley:1.2.1")


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))



    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jsoup:jsoup:1.15.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Add ExoPlayer to play movie
    implementation("com.google.android.exoplayer:exoplayer:2.18.7")
//    implementation ("com.google.android.youtube:youtube-android-player-api:1.2.2")
//    implementation("com.google.android.exoplayer:exoplayer-ui:2.14.1")

//    implementation("androidx.media3:media3-exoplayer:1.3.1")
//    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
//    implementation("androidx.media3:media3-ui:1.3.1")
//
//    implementation("androidx.webkit:webkit:1.9.0")

    //  Handle error by playing movie
    implementation("androidx.multidex:multidex:2.0.1")

    // Android HTML Parser
    implementation("org.htmlparser:htmlparser:2.1")

    implementation("io.github.shashank02051997:FancyToast:2.0.2")
}
