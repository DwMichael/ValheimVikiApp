package com.rabbitv.valheimviki.presentation.detail.creature

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.mapper.CreatureFactory.createFromCreature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.utils.Constants.MAIN_BOSS_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatureScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val creatureUseCases: CreatureUseCases,
) : ViewModel(){
    private val mainBossId: String = checkNotNull(savedStateHandle[MAIN_BOSS_ARGUMENT_KEY])
    private val _mainBoss = MutableStateFlow<MainBoss?>(null)
    val mainBoss: StateFlow<MainBoss?> = _mainBoss

    init {
        viewModelScope.launch(Dispatchers.IO) {
            creatureUseCases.getCreatureById(mainBossId).let {
                    _mainBoss.value = createFromCreature(it)
            }
        }
    }
}