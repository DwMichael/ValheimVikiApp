package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    biomeRepository: BiomeRepository,
) : ViewModel() {

    private val _biome = savedStateHandle.toRoute<Biome>()
    private val _biomeInfo: Flow<Biome> = biomeRepository.getBiomeById(_biome.id)
    val biomeInfo: Flow<Biome> = _biomeInfo


}
