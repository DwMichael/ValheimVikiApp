package com.rabbitv.valheimviki

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.rabbitv.valheimviki.domain.exceptions.WorkerFetchException
import com.rabbitv.valheimviki.domain.exceptions.WorkerInsertException
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.net.UnknownHostException

@HiltWorker
class FetchWorker @AssistedInject constructor(
    @Assisted private val biomeRepository: BiomeRepository,
    @Assisted private val creatureRepository: CreaturesRepository,
    @Assisted private val relationsRepository: RelationsRepository,
    @Assisted private val language: String,
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val response = biomeRepository.fetchBiomes(language)
            val response2 = creatureRepository.fetchCreatures(language)
            val response3 = relationsRepository.fetchRelations()
            if (response.isSuccessful && response2.isSuccessful && response3.isSuccessful) {
                try {
                    biomeRepository.storeBiomes(response.body()!!)
                    creatureRepository.insertCreatures(response2.body()!!)
                    relationsRepository.insertRelations(response3.body()!!)
                    Log.e("FetchWorker", " Success")
                    Result.success()
                } catch (e: Exception) {
                    Result.failure()
                    throw WorkerInsertException("One of inserts in Worker failed : ${e.message}")
                }
            } else {
                Log.e("FetchWorker", "Retrying")
                Result.retry()
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: "No error body"
                throw WorkerFetchException("API in Worker request failed with code $errorCode: $errorBody")
            }
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                Log.e("FetchWorker", "Retrying")
                Result.retry()
            } else {
                Log.e("FetchWorker", "Error")
                Result.failure(Data.Builder().putString("error", e.message).build())
            }
        }
    }
}