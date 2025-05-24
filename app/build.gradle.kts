plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.anticyberbullyingapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.anticyberbullyingapp"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    buildFeatures {
        viewBinding = true
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.appcompat:appcompat:1.6.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.0")
    implementation("com.google.android.material:material:1.11.0")
    debugImplementation("com.google.android.material:material:1.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    debugImplementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    debugImplementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.core:core-ktx:1.12.0")
    debugImplementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    debugImplementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.6.1")
    debugImplementation("com.google.android.material:material:1.6.1")
    implementation("org.apache.commons:commons-text:1.10.0")
    debugImplementation("org.apache.commons:commons-text:1.10.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    debugImplementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    debugImplementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.6.0")
    debugImplementation("com.google.android.material:material:1.6.0")
    implementation("androidx.cardview:cardview:1.0.0")
    debugImplementation("androidx.cardview:cardview:1.0.0")
    implementation("com.microsoft.onnxruntime:onnxruntime-android:1.17.0")
    debugImplementation("com.microsoft.onnxruntime:onnxruntime-android:1.17.0")
    implementation("org.tensorflow:tensorflow-lite:2.12.0")
    debugImplementation("org.tensorflow:tensorflow-lite:2.12.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    debugImplementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("androidx.compose.material3:material3:1.2.0")
    debugImplementation("androidx.compose.material3:material3:1.2.0")
    implementation("com.google.android.material:material:1.11.0")
    debugImplementation("com.google.android.material:material:1.11.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:rules:1.5.0")





















}