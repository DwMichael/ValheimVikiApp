package com.rabbitv.valheimviki.presentation.creatures.aggresive

import androidx.lifecycle.ViewModel
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AggressiveViewModel @Inject constructor(
    private val creatureRepository: CreatureRepository
): ViewModel(){


    init {

    }
}