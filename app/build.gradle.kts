import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val localProperties = gradleLocalProperties(rootDir)

val apiKey = localProperties.getProperty("API_KEY").orEmpty()
val baseUrl = localProperties.getProperty("BASE_URL").orEmpty()

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
	id("com.google.dagger.hilt.android")
	id("androidx.navigation.safeargs.kotlin")
}

android {
	namespace = "my.ym.taskcurrencyexchange"
	compileSdk = 34

	defaultConfig {
		applicationId = "my.ym.taskcurrencyexchange"
		minSdk = 21
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		buildConfigField("String", "API_KEY", "\"$apiKey\"")
		buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
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

	buildFeatures {
		viewBinding = true
		dataBinding = true
		buildConfig = true
	}
}

dependencies {
	// AndroidX
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

	// Lifecycle
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
	implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")
	implementation("androidx.lifecycle:lifecycle-common-java8:2.7.0")

	// UI ( Material design guidelines )
	implementation("com.google.android.material:material:1.11.0")

	// Navigation
	implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
	implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

	// Hilt
	implementation("com.google.dagger:hilt-android:2.48")
	kapt("com.google.dagger:hilt-android-compiler:2.48")

	// Retrofit
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")

	// OkHttp3
	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

	// Timber
	implementation("com.jakewharton.timber:timber:5.0.1")

	// Gson ( Json Serialization & Deserialization )
	implementation("com.google.code.gson:gson:2.10.1")

	// Lottie Loader
	implementation("com.airbnb.android:lottie:6.3.0")

	// ---- Testing ---- //

	testImplementation("junit:junit:4.13.2")

	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Allow references to generated code
kapt {
	correctErrorTypes = true
}
