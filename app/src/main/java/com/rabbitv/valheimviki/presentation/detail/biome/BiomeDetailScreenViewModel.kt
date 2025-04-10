package com.rabbitv.valheimviki.presentation.detail.biome

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.toMainBoss
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.utils.Constants.BIOME_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiomeDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val biomeUseCases: BiomeUseCases,
    private val creaturesUse: CreatureUseCases,
    private val relationsUse: RelationUseCases
) : ViewModel() {
    private val _biome = MutableStateFlow<Biome?>(null)
    val biome: StateFlow<Biome?> = _biome

    private  val _mainBoss = MutableStateFlow<MainBoss?>(null)
    val mainBoss : StateFlow<MainBoss?> = _mainBoss


    init {

        viewModelScope.launch(Dispatchers.IO) {

            val biomeId = async {
                savedStateHandle.get<String>(BIOME_ARGUMENT_KEY).toString()
            }.await()

            _biome.value = biomeId.let { biomeUseCases.getBiomeByIdUseCase(biomeId = biomeId) }

            val deferredMainBoss: Deferred<String> = async {
                biomeId.let { relationsUse.getRelatedIdUseCase(it) }
            }

            val deferredRelation: Deferred<List<String>> = async {
                biomeId.let { relationsUse.getRelatedIdsUseCase(it) }
            }

            val mainBossId: String? = deferredMainBoss.await()
            val relatedObjects: List<String> = deferredRelation.await()

            mainBossId?.let { id ->
                creaturesUse.getCreatureById(id)?.toMainBoss()?.let { boss ->
                    _mainBoss.value = boss
                }
            }

        }
    }
}




