package com.rabbitv.valheimviki

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rabbitv.valheimviki.domain.use_cases.app_update.AppUpdateManager
import com.rabbitv.valheimviki.domain.use_cases.app_update.UpdateState
import com.rabbitv.valheimviki.navigation.ValheimVikiApp
import com.rabbitv.valheimviki.presentation.components.AppUpdateHandler
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    @Inject
    lateinit var appUpdateManager: AppUpdateManager

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        
        // Start background data fetching
        val workRequest = OneTimeWorkRequestBuilder<FetchWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                duration = Duration.ofSeconds(10)
            )
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        
        // Check for app updates
        checkForAppUpdates()
        
        setContent {
            ValheimVikiAppTheme {
                var updateState by remember { mutableStateOf<UpdateState>(UpdateState.NotChecked) }
                
                // Collect update state changes
                LaunchedEffect(Unit) {
                    appUpdateManager.updateState.collectLatest { state ->
                        updateState = state
                    }
                }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        // Main app content
                        ValheimVikiApp()
                        
                        // App update UI overlay
                        AppUpdateHandler(
                            updateState = updateState,
                            onStartImmediateUpdate = { appUpdateInfo ->
                                appUpdateManager.startImmediateUpdate(this@MainActivity, appUpdateInfo)
                            },
                            onStartFlexibleUpdate = { appUpdateInfo ->
                                appUpdateManager.startFlexibleUpdate(this@MainActivity, appUpdateInfo)
                            },
                            onCompleteUpdate = {
                                appUpdateManager.completeFlexibleUpdate()
                            },
                            onDismissUpdate = {
                                appUpdateManager.resetState()
                            }
                        )
                    }
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Resume any in-progress updates
        appUpdateManager.resumeUpdateIfNeeded(this)
    }
    
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle update flow results
        appUpdateManager.handleUpdateResult(requestCode, resultCode)
    }
    
    private fun checkForAppUpdates() {
        lifecycleScope.launch {
            // Small delay to let the app initialize
            kotlinx.coroutines.delay(2000)
            appUpdateManager.checkForAppUpdate()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ValheimVikiAppTheme {

    }
}