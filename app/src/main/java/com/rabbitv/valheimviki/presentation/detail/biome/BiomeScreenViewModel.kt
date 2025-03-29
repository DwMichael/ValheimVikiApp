package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    biomeRepository: BiomeRepository,
) : ViewModel() {

    private val _biome = savedStateHandle.toRoute<Biome>()
    private val _biomeInfo = MutableStateFlow<Biome?>(null)
    val biomeInfo: StateFlow<Biome?> get() = _biomeInfo

    init {
        viewModelScope.launch {
            biomeRepository.getBiomeById(_biome.toString()).collect {
                _biomeInfo.value = it
            }
        }
    }




}
