import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	id("com.google.devtools.ksp")
	kotlin("plugin.serialization") version "2.2.0"
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "com.rabbitv.valheimviki"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.rabbitv.valheimviki"
		minSdk = 26
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "com.rabbitv.valheimviki.CustomTestRunner"
	}

	base {
		archivesName = "Translator -v${defaultConfig.versionCode}"
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
	kotlin {
		compilerOptions {
			jvmTarget = JvmTarget.fromTarget("17")
		}
	}
	buildFeatures {
		compose = true
	}
	testOptions {
		unitTests {
			isIncludeAndroidResources = true
			isReturnDefaultValues = true
		}
	}

}
val mockitoAgent: Configuration by configurations.creating {
	isCanBeConsumed = false
	isCanBeResolved = true
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
	implementation(libs.androidx.compose.material)
	testImplementation(libs.junit.jupiter)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)


	///IF SOMETHING AGAIN WILL TURN OFF BC OF LIBRARIES THEN USE DOES UNDER WITH NEWER VERSION

	implementation(libs.okhttp)
	implementation("com.squareup.retrofit2:retrofit:3.0.0")
	implementation("com.squareup.retrofit2:converter-gson:3.0.0")
	implementation("androidx.core:core-splashscreen:1.0.1")
	implementation("androidx.navigation:navigation-compose:2.9.0")
	androidTestImplementation("androidx.navigation:navigation-testing:2.9.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
	implementation("com.google.dagger:hilt-android:2.56.2")
//    ksp("com.google.dagger:hilt-compiler:2.56.1")
	ksp("com.google.dagger:hilt-android-compiler:2.56.2")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

	
	implementation("androidx.constraintlayout:constraintlayout:2.2.1")
	implementation("com.composables:icons-lucide:1.1.0")


	implementation("androidx.work:work-runtime-ktx:2.10.2")
	androidTestImplementation("androidx.work:work-testing:2.10.2")

	implementation("io.coil-kt.coil3:coil-compose:3.2.0")
	implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0")

	implementation("androidx.compose.ui:ui-text-google-fonts:1.8.3")
	implementation("androidx.datastore:datastore-preferences:1.1.7")
	implementation("androidx.room:room-runtime:2.7.2")
	implementation("androidx.room:room-ktx:2.7.2")






	androidTestImplementation("com.google.dagger:hilt-android-testing:2.56.2")
	ksp("androidx.room:room-compiler:2.7.2")

	kspTest("com.google.dagger:hilt-compiler:2.56.2")
	kspAndroidTest("com.google.dagger:hilt-compiler:2.56.2")
	implementation("androidx.hilt:hilt-work:1.2.0")

	testImplementation("org.mockito:mockito-core:5.18.0")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")


	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0") {
		exclude(group = "org.mockito", module = "mockito-android")
	}
	// Kotlin Test with JUnit 5 (includes JUnit 5 engine)
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.2.0")
	// Turbine for Flow testing
	testImplementation("app.cash.turbine:turbine:1.2.1")

	testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
	mockitoAgent("org.mockito:mockito-core:5.18.0") { isTransitive = false }
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()   // if you are on JUnit 5
	jvmArgs(
		// needed since JDK 16 to let agents transform java.lang.*
		"--add-opens", "java.base/java.lang=ALL-UNNAMED",
		// pass the absolute path of the single JAR in our configuration
		"-javaagent:${mockitoAgent.singleFile.absolutePath}"
	)
}
