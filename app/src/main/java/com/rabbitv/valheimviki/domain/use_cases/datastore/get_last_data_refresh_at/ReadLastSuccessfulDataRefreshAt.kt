package com.rabbitv.valheimviki.domain.use_cases.datastore.get_last_data_refresh_at

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadLastSuccessfulDataRefreshAt @Inject constructor(
	private val repository: DataStoreRepository
) {
	operator fun invoke(): Flow<Long> =
		repository.readLastSuccessfulDataRefreshAt()
}
