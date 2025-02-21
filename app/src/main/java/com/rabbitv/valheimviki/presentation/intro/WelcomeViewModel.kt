package com.rabbitv.valheimviki.presentation.intro

import androidx.lifecycle.ViewModel
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val useCases: DataStoreUseCases
) : ViewModel() {

    suspend fun saveOnBoardingState(completed: Boolean) {
        withContext(Dispatchers.IO) {
            useCases.saveOnBoardingState(completed = completed)
        }
    }
}
