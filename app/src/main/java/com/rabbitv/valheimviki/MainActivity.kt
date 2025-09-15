package com.rabbitv.valheimviki

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.rabbitv.valheimviki.navigation.ValheimVikiApp
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
	private lateinit var appUpdateManager: AppUpdateManager
	private val updateType = AppUpdateType.FLEXIBLE

	@RequiresApi(Build.VERSION_CODES.S)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen()
		enableEdgeToEdge()
		
		// Initialize AppUpdateManager
		appUpdateManager = AppUpdateManagerFactory.create(this)
		
		val workRequest = OneTimeWorkRequestBuilder<FetchWorker>()
			.setInitialDelay(5, TimeUnit.SECONDS)
			.setBackoffCriteria(
				backoffPolicy = BackoffPolicy.EXPONENTIAL,
				duration = Duration.ofSeconds(10)
			)
			.build()
		WorkManager.getInstance(applicationContext).enqueue(workRequest)
		
		if (updateType == AppUpdateType.FLEXIBLE) {
			appUpdateManager.registerListener(installStateUpdatedListener)
		}
		
		// Check for updates after a short delay
		lifecycleScope.launch {
			delay(2000) // Wait 2 seconds for app to initialize
			checkForAppUpdates()
		}
		
		setContent {
			ValheimVikiApp()
		}
	}

	private fun checkForAppUpdates() {
		Log.d("UpdateManager", "🔍 Checking for app updates...")
		appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
			Log.d("UpdateManager", "📱 Update info received:")
			Log.d("UpdateManager", "   - Update availability: ${info.updateAvailability()}")
			Log.d("UpdateManager", "   - Staleness days: ${info.clientVersionStalenessDays()}")
			Log.d("UpdateManager", "   - Flexible allowed: ${info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)}")
			Log.d("UpdateManager", "   - Immediate allowed: ${info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)}")
			
			val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
			val isUpdateAllowed = when (updateType) {
				AppUpdateType.FLEXIBLE -> info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
				AppUpdateType.IMMEDIATE -> info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
				else -> false
			}
			
			if (isUpdateAvailable && isUpdateAllowed) {
				Log.d("UpdateManager", "✅ Update available! Starting update flow...")
				appUpdateManager.startUpdateFlowForResult(
					info,
					updateType,
					this,
					123
				)
			} else {
				Log.d("UpdateManager", "❌ No update available or not allowed")
			}
		}.addOnFailureListener { exception ->
			Log.e("UpdateManager", "💥 Failed to check for updates", exception)
		}
	}

	private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
		Log.d("UpdateManager", "📥 Install status: ${state.installStatus()}")
		if (state.installStatus() == InstallStatus.DOWNLOADED) {
			Log.d("UpdateManager", "✅ Update downloaded! Showing restart message...")
			Toast.makeText(
				this,
				"Download Complete successfully. Restarting app in 5 seconds.",
				Toast.LENGTH_LONG
			).show()
			lifecycleScope.launch {
				delay(5.seconds)
				Log.d("UpdateManager", "🔄 Completing update...")
				appUpdateManager.completeUpdate()
			}
		}
	}

	override fun onResume() {
		super.onResume()
		if (updateType == AppUpdateType.IMMEDIATE) {
			appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
				if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
					appUpdateManager.startUpdateFlowForResult(
						info,
						updateType,
						this,
						123
					)
				}
			}
		}
	}

	@Deprecated("Deprecated in Java")
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == 123) {
			Log.d("UpdateManager", "📋 Update result: $resultCode")
			if (resultCode == RESULT_OK) {
				Log.d("UpdateManager", "✅ Update flow completed successfully")
			} else {
				Log.d("UpdateManager", "❌ Update flow failed or was cancelled")
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		if (updateType == AppUpdateType.FLEXIBLE) {
			appUpdateManager.unregisterListener(installStateUpdatedListener)
		}
	}
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	ValheimVikiAppTheme {

	}
}