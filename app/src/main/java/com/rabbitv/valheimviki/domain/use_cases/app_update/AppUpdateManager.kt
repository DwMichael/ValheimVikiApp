package com.rabbitv.valheimviki.domain.use_cases.app_update

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUpdateManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val TAG = "AppUpdateManager"
        const val UPDATE_REQUEST_CODE = 1001
        
        // Minimum number of days after which a flexible update should be forced
        private const val DAYS_FOR_FLEXIBLE_UPDATE = 3
    }
    
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)
    
    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.NotChecked)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()
    
    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                Log.d(TAG, "Update downloading...")
                _updateState.value = UpdateState.Downloading
            }
            InstallStatus.DOWNLOADED -> {
                Log.d(TAG, "Update downloaded, ready to install")
                _updateState.value = UpdateState.ReadyToInstall
            }
            InstallStatus.INSTALLING -> {
                Log.d(TAG, "Update installing...")
                _updateState.value = UpdateState.Installing
            }
            InstallStatus.INSTALLED -> {
                Log.d(TAG, "Update installed successfully")
                _updateState.value = UpdateState.Installed
                unregisterListener()
            }
            InstallStatus.FAILED -> {
                Log.e(TAG, "Update failed")
                _updateState.value = UpdateState.Failed("Update installation failed")
                unregisterListener()
            }
            InstallStatus.CANCELED -> {
                Log.d(TAG, "Update canceled by user")
                _updateState.value = UpdateState.Canceled
                unregisterListener()
            }
        }
    }
    
    /**
     * Check if an app update is available
     */
    fun checkForAppUpdate() {
        Log.d(TAG, "Checking for app updates...")
        
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            Log.d(TAG, "Update info received: availability=${appUpdateInfo.updateAvailability()}")
            
            when {
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE -> {
                    Log.d(TAG, "Update available")
                    handleUpdateAvailable(appUpdateInfo)
                }
                appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                    Log.d(TAG, "Update already in progress")
                    _updateState.value = UpdateState.InProgress
                }
                else -> {
                    Log.d(TAG, "No update available")
                    _updateState.value = UpdateState.NotAvailable
                }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Failed to check for updates", exception)
            _updateState.value = UpdateState.Failed("Failed to check for updates: ${exception.message}")
        }
    }
    
    private fun handleUpdateAvailable(appUpdateInfo: AppUpdateInfo) {
        val clientVersionStalenessDays = appUpdateInfo.clientVersionStalenessDays() ?: 0
        
        when {
            // Force immediate update for critical updates or old versions
            shouldForceImmediateUpdate(appUpdateInfo, clientVersionStalenessDays) -> {
                Log.d(TAG, "Immediate update required")
                _updateState.value = UpdateState.ImmediateUpdateRequired(appUpdateInfo)
            }
            // Flexible update for regular updates
            appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> {
                Log.d(TAG, "Flexible update available")
                _updateState.value = UpdateState.FlexibleUpdateAvailable(appUpdateInfo)
            }
            else -> {
                Log.d(TAG, "Update available but not supported")
                _updateState.value = UpdateState.NotAvailable
            }
        }
    }
    
    private fun shouldForceImmediateUpdate(appUpdateInfo: AppUpdateInfo, stalenessDays: Int): Boolean {
        return when {
            // If immediate update is not supported, return false
            !appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> false
            // Force immediate update if app is older than threshold
            stalenessDays >= DAYS_FOR_FLEXIBLE_UPDATE -> true
            // Check if this is a high priority update
            appUpdateInfo.updatePriority() >= 4 -> true
            else -> false
        }
    }
    
    /**
     * Start immediate update flow
     */
    fun startImmediateUpdate(activity: Activity, appUpdateInfo: AppUpdateInfo) {
        try {
            Log.d(TAG, "Starting immediate update...")
            _updateState.value = UpdateState.InProgress
            
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                activity,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                UPDATE_REQUEST_CODE
            )
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Failed to start immediate update", e)
            _updateState.value = UpdateState.Failed("Failed to start update: ${e.message}")
        }
    }
    
    /**
     * Start flexible update flow
     */
    fun startFlexibleUpdate(activity: Activity, appUpdateInfo: AppUpdateInfo) {
        try {
            Log.d(TAG, "Starting flexible update...")
            _updateState.value = UpdateState.InProgress
            
            registerListener()
            
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                activity,
                AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                UPDATE_REQUEST_CODE
            )
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Failed to start flexible update", e)
            _updateState.value = UpdateState.Failed("Failed to start update: ${e.message}")
            unregisterListener()
        }
    }
    
    /**
     * Complete flexible update installation
     */
    fun completeFlexibleUpdate() {
        Log.d(TAG, "Completing flexible update installation...")
        appUpdateManager.completeUpdate()
    }
    
    /**
     * Handle activity result from update flow
     */
    fun handleUpdateResult(requestCode: Int, resultCode: Int) {
        if (requestCode == UPDATE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "Update flow completed successfully")
                    // For immediate updates, this means the update was successful
                    // For flexible updates, the download will start
                }
                Activity.RESULT_CANCELED -> {
                    Log.d(TAG, "Update flow canceled by user")
                    _updateState.value = UpdateState.Canceled
                }
                else -> {
                    Log.e(TAG, "Update flow failed with result code: $resultCode")
                    _updateState.value = UpdateState.Failed("Update failed with code: $resultCode")
                }
            }
        }
    }
    
    /**
     * Resume update if one was paused
     */
    fun resumeUpdateIfNeeded(activity: Activity) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                Log.d(TAG, "Resuming update in progress")
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startImmediateUpdate(activity, appUpdateInfo)
                }
            }
        }
    }
    
    private fun registerListener() {
        appUpdateManager.registerListener(installStateUpdatedListener)
    }
    
    private fun unregisterListener() {
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }
    
    /**
     * Reset update state
     */
    fun resetState() {
        _updateState.value = UpdateState.NotChecked
    }
}

/**
 * Represents different states of the app update process
 */
sealed class UpdateState {
    object NotChecked : UpdateState()
    object NotAvailable : UpdateState()
    object InProgress : UpdateState()
    object Downloading : UpdateState()
    object ReadyToInstall : UpdateState()
    object Installing : UpdateState()
    object Installed : UpdateState()
    object Canceled : UpdateState()
    data class Failed(val message: String) : UpdateState()
    data class ImmediateUpdateRequired(val appUpdateInfo: AppUpdateInfo) : UpdateState()
    data class FlexibleUpdateAvailable(val appUpdateInfo: AppUpdateInfo) : UpdateState()
}
