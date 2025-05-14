package com.rabbitv.valheimviki.presentation.weapons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeaponListViewModel @Inject constructor(
    private val weaponRepository: WeaponUseCases,
    private val connectivityObserver: NetworkConnectivity,
) : ViewModel() {
    private val _isConnection: StateFlow<Boolean> = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _weapons: MutableStateFlow<List<Weapon>> =
        MutableStateFlow<List<Weapon>>(emptyList())
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)
    private val _error: MutableStateFlow<String?> = MutableStateFlow<String?>(null)


    val uiState = combine(
        _weapons,
        _isConnection,
        _isLoading,
        _error
    ) { weapons, isConnection, isLoading, error ->
        WeaponListUiState(
            weaponList = weapons,
            isConnection = isConnection,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeaponListUiState()
    )
}