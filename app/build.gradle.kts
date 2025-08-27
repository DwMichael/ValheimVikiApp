import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	id("com.google.devtools.ksp")
	kotlin("plugin.serialization") version "2.2.10"
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "com.rabbitv.valheimviki"
	compileSdk = 36

	val file = rootProject.file("local.properties")
	val properties = Properties()
	properties.load(FileInputStream(file))

	defaultConfig {
		applicationId = "com.rabbitv.valheimviki"
		minSdk = 26
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		buildConfigField("String", "baseUrlSafe", properties.getProperty("baseUrl"))

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
		buildConfig = true
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
	implementation(libs.androidx.navigation.testing)
	testImplementation(libs.junit.jupiter)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)


	implementation(libs.androidx.paging.runtime)
	testImplementation(libs.paging.common)
	testImplementation(libs.androidx.paging.testing)
	implementation(libs.paging.compose)
	implementation(libs.room.paging)

	implementation(libs.okhttp)
	implementation(libs.retrofit)
	implementation(libs.converter.gson)
	implementation(libs.androidx.core.splashscreen)
	implementation(libs.androidx.navigation.compose)
	androidTestImplementation(libs.androidx.navigation.testing)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.hilt.android)
//    ksp("com.google.dagger:hilt-compiler:2.56.1")
	ksp(libs.dagger.hilt.android.compiler)
	implementation(libs.androidx.hilt.navigation.compose)

	implementation(libs.androidx.appcompat)
	implementation(libs.icons.lucide)


	implementation(libs.androidx.work.runtime.ktx)
	androidTestImplementation(libs.work.testing)

	implementation(libs.coil.compose)
	implementation(libs.coil3.coil.network.okhttp)

	implementation(libs.androidx.ui.text.google.fonts)
	implementation(libs.androidx.datastore.preferences)
	implementation(libs.androidx.room.runtime)
	implementation(libs.room.ktx)
	implementation(libs.androidx.security.crypto)

	androidTestImplementation(libs.dagger.hilt.android.testing)
	ksp(libs.androidx.room.compiler)

	kspTest(libs.dagger.hilt.compiler)
	kspAndroidTest(libs.com.google.dagger.hilt.compiler)
	implementation(libs.androidx.hilt.work)

	testImplementation(libs.mockito.core)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.junit)
	testImplementation(libs.robolectric)

	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0") {
		exclude(group = "org.mockito", module = "mockito-android")
	}
	// Kotlin Test with JUnit 5 (includes JUnit 5 engine)
	testImplementation(libs.kotlin.test.junit5)
	// Turbine for Flow testing
	testImplementation(libs.turbine)

	testImplementation(libs.mockito.junit.jupiter)

	implementation(libs.androidx.runtime.tracing)

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
