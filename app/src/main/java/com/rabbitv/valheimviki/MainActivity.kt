package com.rabbitv.valheimviki

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rabbitv.valheimviki.navigation.ValheimVikiApp
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        val workRequest = OneTimeWorkRequestBuilder<FetchWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                duration = Duration.ofSeconds(10)
            )
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        setContent {
            ValheimVikiApp()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ValheimVikiAppTheme {

    }
}