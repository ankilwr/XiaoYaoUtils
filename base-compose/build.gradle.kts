plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

val libVersions: Map<String, String> by rootProject.extra

android {
    namespace = "com.mellivora.base.compose"

    defaultConfig {
        compileSdk = 34
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures{
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libVersions["compose_compiler_version"]
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    api(project(":base-core"))

//    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
//    implementation(composeBom)
//    androidTestImplementation(composeBom)
//    api("androidx.compose.material3:material3")
//    debugApi("androidx.compose.ui:ui-tooling")
//    api("androidx.compose.ui:ui")

    //compose
    debugApi("androidx.compose.ui:ui-tooling:${libVersions["compose_compiler_version"]}")
    api("androidx.compose.ui:ui:${libVersions["compose_compiler_version"]}")
    api("androidx.compose.material:material:${libVersions["compose_compiler_version"]}")
    api("androidx.activity:activity-compose:1.7.2")
//    api("androidx.compose.ui.graphics.animation:${libVersions["compose_compiler_version"]}")

    api("com.google.accompanist:accompanist-insets:0.16.0")
    api("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    api("com.google.accompanist:accompanist-drawablepainter:0.30.1")

    //compose网络图片加载库
    api("io.coil-kt:coil-compose:2.4.0")
    //livedata转state
    api("androidx.compose.runtime:runtime-livedata:1.6.0-alpha04")

    //SVGA动画
    implementation("com.github.yyued:SVGAPlayer-Android:2.6.1")
}