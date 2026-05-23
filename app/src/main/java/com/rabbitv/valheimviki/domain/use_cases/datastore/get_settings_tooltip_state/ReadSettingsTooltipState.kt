package com.rabbitv.valheimviki.domain.use_cases.datastore.get_settings_tooltip_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadSettingsTooltipState @Inject constructor(
    private val repository: DataStoreRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.readSettingsTooltipStep()
    }
}
