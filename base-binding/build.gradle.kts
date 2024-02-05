plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    namespace = "com.mellivora.base.binding"

    defaultConfig {
        compileSdk = 33
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures{
        viewBinding = true
        dataBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    api("com.google.android.material:material:1.9.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.recyclerview:recyclerview:1.3.1")
    api("androidx.viewpager2:viewpager2:1.1.0-beta02")
    api("androidx.media:media:1.6.0")
    api("com.google.android:flexbox:1.1.0")
    api("androidx.dynamicanimation:dynamicanimation-ktx:1.0.0-alpha03")

    //adapter适配器
    api("com.drakeet.multitype:multitype:4.2.0")

    api(project(":base-core"))

    //图片加载库
    api("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    //Glide扩展库(高斯模糊)
    api("jp.wasabeef:glide-transformations:4.3.0") {
        //这个库里面用了Glide4.9.0
        exclude(group = "com.github.bumptech.glide")
    }
    //SVGA动画
    api("com.github.yyued:SVGAPlayer-Android:2.6.1")

    //下拉刷新
    api("io.github.scwang90:refresh-layout-kernel:2.0.6")
    api("io.github.scwang90:refresh-header-classics:2.0.6")
}

//是否为发行版本的库【false；发布快照版本，true:发布release版本】
val releaseVersion = false
afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.ankilwr.XiaoYaoUtils"
                artifactId = "binding"
                val libraryVersion = "1.0.0"
                version = if(releaseVersion) libraryVersion else "$libraryVersion-SNAPSHOT"
            }
        }
    }
}
