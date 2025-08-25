package com.rabbitv.valheimviki

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.rabbitv.valheimviki.domain.model.data_refetch_result.DataRefetchResult
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FetchWorker @AssistedInject constructor(
	@Assisted private val dataRefetchUseCase: DataRefetchUseCase,
	@Assisted private val context: Context,
	@Assisted private val params: WorkerParameters
) : CoroutineWorker(context, params) {
	override suspend fun doWork(): Result {
		return try {
			val result = dataRefetchUseCase.refetchAllData()
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
}