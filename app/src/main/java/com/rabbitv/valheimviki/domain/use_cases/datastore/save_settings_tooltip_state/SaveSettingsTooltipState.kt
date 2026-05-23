package com.rabbitv.valheimviki.domain.use_cases.datastore.save_settings_tooltip_state

import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import javax.inject.Inject

class SaveSettingsTooltipState @Inject constructor(
    private val repository: DataStoreRepository
) {
    suspend operator fun invoke(step: Int) {
        repository.saveSettingsTooltipStep(step)
    }
}
