package com.rabbitv.valheimviki.presentation.detail.creature

import androidx.lifecycle.ViewModel
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatureScreenViewModel @Inject constructor(
    creatureRepository: CreatureRepository,
) : ViewModel()