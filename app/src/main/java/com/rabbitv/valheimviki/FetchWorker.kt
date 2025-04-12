package com.rabbitv.valheimviki

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException
import java.net.UnknownHostException

@HiltWorker
class FetchWorker @AssistedInject constructor(
    @Assisted private val biomeRepository: BiomeRepository,
    @Assisted private val creatureRepository: CreaturesRepository,
    @Assisted private val relationsRepository: RelationsRepository,
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val response = biomeRepository.fetchBiomes("en")
            val response2 = creatureRepository.fetchCreatures("en")
            val response3 = relationsRepository.fetchRelations()
            if (response.isSuccessful && response2.isSuccessful && response3.isSuccessful) {
                    biomeRepository.storeBiomes(response.body()!!)
                    creatureRepository.insertCreatures(response2.body()!!)
                    relationsRepository.insertRelations(response3.body()!!)
                    Log.e("FetchWorker", " Success")
                    Result.success()
            } else {
                Log.e("FetchWorker", "Retrying")
                Result.retry()
            }
        } catch (e: Exception) {
            if (e is UnknownHostException
                || e is FetchException
                || e is IOException) {
                Log.e("FetchWorker", "Retrying")
                return Result.retry()
            } else {
                Log.e("FetchWorker", "Error")
                return Result.failure(
                    Data.Builder().putString("error", e.message).build()
                )
            }

        }
    }
}