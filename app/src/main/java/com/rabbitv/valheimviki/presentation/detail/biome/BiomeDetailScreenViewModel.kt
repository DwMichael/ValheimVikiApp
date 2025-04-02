package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiomeDetailScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val biomeUseCases: BiomeUseCases,
    private val creaturesUse: CreatureUseCases
) : ViewModel() {
    private val _biomeId: String = checkNotNull(savedStateHandle[BIOME_ARGUMENT_KEY])
    private val _biome = MutableStateFlow<Biome?>(null)
    val biome: StateFlow<Biome?> = _biome

    private  val _mainBossId = MutableStateFlow<MainBoss?>(null)
    val mainBossId : StateFlow<MainBoss?> = _mainBossId


    init {
        viewModelScope.launch {
            biomeUseCases.getBiomeByIdUseCase(_biomeId).collect {
                 _biome.value = it
                _mainBossId.value = it.
            }
            creaturesUse.getMainBossesByIdUseCase()
        }
    }



    }

}

