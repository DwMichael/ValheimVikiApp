package com.rabbitv.valheimviki.domain.use_cases.update_manager

import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import jakarta.inject.Inject

class UpdateManager @Inject constructor(
	private val context: Context
) {
	private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)

	companion object {
		private const val DAYS_FOR_FLEXIBLE_UPDATE = 2
	}

	/**
	 * Sprawdza dostępność aktualizacji i uruchamia elastyczny proces aktualizacji.
	 *
	 * @param activity Aktywność, która jest potrzebna do uruchomienia okna aktualizacji.
	 */
	fun checkForUpdate(activity: Activity) {
		appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
			if (isUpdateAvailable(appUpdateInfo)) {
				startFlexibleUpdate(appUpdateInfo, activity)
			}
		}
	}

	private fun isUpdateAvailable(appUpdateInfo: AppUpdateInfo): Boolean {
		return appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
				(appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE &&
				appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
	}

	private fun startFlexibleUpdate(appUpdateInfo: AppUpdateInfo, activity: Activity) {
		appUpdateManager.startUpdateFlowForResult(
			appUpdateInfo,
			activity,
			AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
			0 // Request code, możesz użyć dowolnej wartości
		)
	}
}