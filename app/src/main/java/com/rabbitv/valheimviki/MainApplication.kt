package com.rabbitv.valheimviki

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject


@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var  workerFactory: FetchWorkerFactory

    override val workManagerConfiguration: Configuration
        get() =  Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}


class FetchWorkerFactory @Inject constructor(
   private val biomeRepository: BiomeRepository,
   private val creatureRepository: CreaturesRepository,
   private val relationsRepository: RelationsRepository,
    private val language: String,
): WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = FetchWorker(biomeRepository,creatureRepository,relationsRepository,language,appContext,workerParameters)
}