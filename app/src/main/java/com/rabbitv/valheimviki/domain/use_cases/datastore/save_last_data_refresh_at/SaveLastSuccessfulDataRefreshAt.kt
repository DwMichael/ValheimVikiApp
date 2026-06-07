package com.rabbitv.valheimviki.domain.use_cases.datastore.save_last_data_refresh_at

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import javax.inject.Inject

class SaveLastSuccessfulDataRefreshAt @Inject constructor(
	private val repository: DataStoreRepository
) {
	suspend operator fun invoke(timestampMillis: Long) {
		repository.saveLastSuccessfulDataRefreshAt(timestampMillis)
	}
}
