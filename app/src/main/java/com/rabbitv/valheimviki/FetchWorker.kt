package com.rabbitv.valheimviki

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.rabbitv.valheimviki.domain.model.data_refetch_result.DataRefetchResult
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class FetchWorker @AssistedInject constructor(
	private val dataRefetchUseCase: DataRefetchUseCase,
	private val dataStoreUseCases: DataStoreUseCases,
	@Assisted appContext: Context,
	@Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {
	override suspend fun doWork(): Result {
		return try {
			val result = dataRefetchUseCase.refetchAllData(forceRefresh = shouldForceRefresh())
			when (result) {
				is DataRefetchResult.Success -> {
					Log.d("FetchWorker", "Data refresh successful")
					Result.success()
				}

				is DataRefetchResult.PartialSuccess -> {
					Log.w(
						"FetchWorker",
						"Partial data refresh: ${result.successfulCategories.size}/${result.totalCategories} categories successful"
					)
					Log.w(
						"FetchWorker",
						"Failed categories: ${result.failedCategories.keys.joinToString(", ")}"
					)
					Result.success()
				}

				is DataRefetchResult.NetworkError -> {
					Log.e("FetchWorker", "Network error: ${result.message}")
					Result.retry()
				}

				is DataRefetchResult.Error -> {
					Log.e("FetchWorker", "Error: ${result.message}")
					Result.failure()
				}
			}
		} catch (e: Exception) {
			Log.e("FetchWorker", "Unexpected error: ${e.message}")
			return Result.failure(Data.Builder().putString("error", e.message).build())
		}
	}

	private suspend fun shouldForceRefresh(): Boolean {
		val lastSuccessfulRefresh = dataStoreUseCases.readLastSuccessfulDataRefreshAt().first()
		if (lastSuccessfulRefresh <= 0L) return false
		val ageMillis = System.currentTimeMillis() - lastSuccessfulRefresh
		return ageMillis >= STALE_DATA_REFRESH_INTERVAL_MS
	}

	companion object {
		private const val STALE_DATA_REFRESH_INTERVAL_MS = 24L * 60L * 60L * 1000L
		const val INITIAL_FETCH_WORK_TAG = "initial_data_refresh"
	}
}
