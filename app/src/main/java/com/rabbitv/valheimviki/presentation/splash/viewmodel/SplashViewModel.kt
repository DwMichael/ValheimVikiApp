package com.rabbitv.valheimviki.presentation.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
	private val useCases: DataStoreUseCases,
	@param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _onBoardingCompleted = MutableStateFlow(false)
	val onBoardingCompleted: StateFlow<Boolean> = _onBoardingCompleted

	init {
		viewModelScope.launch(defaultDispatcher) {
			_onBoardingCompleted.value =
				useCases.readOnBoardingUseCase().stateIn(viewModelScope).value

		}
	}
}