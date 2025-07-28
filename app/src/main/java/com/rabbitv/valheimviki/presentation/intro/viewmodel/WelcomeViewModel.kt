package com.rabbitv.valheimviki.presentation.intro.viewmodel

import androidx.lifecycle.ViewModel
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
	private val useCases: DataStoreUseCases
) : ViewModel() {

	suspend fun saveOnBoardingState(completed: Boolean) {
		useCases.saveOnBoardingState(completed = completed)
	}
}