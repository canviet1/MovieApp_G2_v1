plugins {
    // Apply Android application plugin
    id("com.android.application")
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
    val ext = object {
        val roomVersion = "2.6.1"
        val appCompatVersion = "1.6.1"
        val constraintLayoutVersion = "2.1.4"
        val materialVersion = "1.8.0"
        val lifecycleVersion = "2.5.1"
        val coreTestingVersion = "2.1.0"
        val junitVersion = "4.13.2"
        val espressoVersion = "3.5.1"
        val androidxJunitVersion = "1.1.5"
    }

    // Core Android dependencies
    implementation("androidx.appcompat:appcompat:${ext.appCompatVersion}")
    implementation("com.google.android.material:material:${ext.materialVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${ext.constraintLayoutVersion}")

    // Room components
    implementation("androidx.room:room-runtime:${ext.roomVersion}")
    annotationProcessor("androidx.room:room-compiler:${ext.roomVersion}")
    androidTestImplementation("androidx.room:room-testing:${ext.roomVersion}")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel:${ext.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata:${ext.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${ext.lifecycleVersion}")

    // Testing
    testImplementation("junit:junit:${ext.junitVersion}")
    androidTestImplementation("androidx.arch.core:core-testing:${ext.coreTestingVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${ext.espressoVersion}") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
    androidTestImplementation("androidx.test.ext:junit:${ext.androidxJunitVersion}")

    // Additional libraries
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jsoup:jsoup:1.15.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("org.htmlparser:htmlparser:2.1")
    implementation("io.github.shashank02051997:FancyToast:2.0.2")

    // ExoPlayer for video playback
    implementation("com.google.android.exoplayer:exoplayer:2.18.7")
}
