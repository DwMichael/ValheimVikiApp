import java.io.FileInputStream
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.compose)
	id("com.google.devtools.ksp") version "2.3.6"
	kotlin("plugin.serialization") version "2.3.20"
	id("com.google.dagger.hilt.android") version "2.59.2"
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
		versionCode = 21
		versionName = "1.1.4"

		buildConfigField("String", "baseUrlSafe", properties.getProperty("baseUrl"))

		testInstrumentationRunner = "com.rabbitv.valheimviki.CustomTestRunner"
	}

	base {
		archivesName = "Translator -v${defaultConfig.versionCode}"
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			isShrinkResources = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
			ndk {
				debugSymbolLevel = "SYMBOL_TABLE"
			}
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
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

	implementation(libs.play.services.ads)

	implementation(libs.app.update)
	implementation(libs.app.update.ktx)

	implementation(libs.integrity)

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
	ksp(libs.dagger.hilt.android.compiler)
	implementation(libs.androidx.hilt.navigation.compose)

	implementation(libs.androidx.appcompat)
	implementation(libs.icons.lucide)

	implementation(libs.asset.delivery)
	implementation(libs.asset.delivery.ktx)
	implementation(libs.feature.delivery)
	implementation(libs.feature.delivery.ktx)
	implementation(libs.review)
	implementation(libs.review.ktx)

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

	testImplementation(libs.kotlin.test.junit5)
	testImplementation(libs.turbine)
	testImplementation(libs.mockito.junit.jupiter)

	implementation(libs.androidx.runtime.tracing)

	mockitoAgent("org.mockito:mockito-core:5.18.0") { isTransitive = false }
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
	jvmArgs(
		"--add-opens", "java.base/java.lang=ALL-UNNAMED",
		"-javaagent:${mockitoAgent.singleFile.absolutePath}"
	)
}