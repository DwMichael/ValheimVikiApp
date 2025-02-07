package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.lifecycle.ViewModel
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    biomeRepository: BiomeRepository,
) : ViewModel()