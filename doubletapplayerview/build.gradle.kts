plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "com.ahmedaa612.media3doubletap.dtpv"
    compileSdk = 34

    defaultConfig {
        minSdk = 16
        targetSdk = 34
        vectorDrawables {
            useSupportLibrary = true
        }
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
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

    // Expose the release variant for publishing
    publishing {
        singleVariant("release") {
            // Optionally, generate sources and Javadoc jars
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
}

publishing {
    publications {
        // Register a Maven publication named "release"
        register<MavenPublication>("release") {
            groupId = "io.jitpack"
            artifactId = "library"
            version = "1.0"

            // Use afterEvaluate to ensure that the Android components are configured
            project.afterEvaluate {
                from(components["release"])
            }
        }
    }
}
