package com.rabbitv.valheimviki.presentation.detail.biome

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.data.mappers.toMainBoss
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.CreatureType
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
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
    private val creaturesUseCase: CreatureUseCases,
    private val relationsUseCase: RelationUseCases,
    private val oreDepositUseCases: OreDepositUseCases
) : ViewModel() {
    private val _biome = MutableStateFlow<Biome?>(null)
    val biome: StateFlow<Biome?> = _biome

    private  val _mainBoss = MutableStateFlow<MainBoss?>(null)
    val mainBoss : StateFlow<MainBoss?> = _mainBoss

    private  val _relatedCreatures = MutableStateFlow<List<Creature>>(emptyList())
    val relatedCreatures : StateFlow<List<Creature>> = _relatedCreatures

    private val _relatedOreDeposits = MutableStateFlow<List<OreDeposit>>(emptyList())
    val relatedOreDeposits : StateFlow<List<OreDeposit>> = _relatedOreDeposits

    init {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val biomeId = savedStateHandle.get<String>(BIOME_ARGUMENT_KEY).toString()

                _biome.value = biomeId.let { biomeUseCases.getBiomeByIdUseCase(biomeId = biomeId) }


                val deferredRelations: Deferred<List<String>> = async {
                    biomeId.let { relationsUseCase.getRelatedIdsUseCase(it) }
                }

                val relatedObjects: List<String> = deferredRelations.await()

                try {
                    relatedObjects.let { ids ->
                        creaturesUseCase.getCreatureByRelationAndSubCategory(
                            ids,
                            CreatureType.BOSS)?.toMainBoss().let { boss ->
                            _mainBoss.value = boss
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Boss fetch error BiomeDetailViewModel", e.message.toString())
                    _mainBoss.value = null
                }

                try {
                    val creatures = creaturesUseCase.getCreaturesByIds(relatedObjects)
                    _relatedCreatures.value = creatures
                } catch (e: Exception) {
                    Log.e("Creatures fetch error BiomeDetailViewModel", e.message.toString())
                    _relatedCreatures.value = emptyList()
                }

                try {
                    val oreDeposits = oreDepositUseCases.getOreDepositsByIdsUseCase(relatedObjects)
                    _relatedOreDeposits.value = oreDeposits
                } catch (e: Exception) {
                    Log.e("Ore deposits fetch error BiomeDetailViewModel", e.message.toString())
                    _relatedOreDeposits.value = emptyList()
                }

            } catch (e: Exception) {
                Log.e("General fetch error BiomeDetailViewModel", e.message.toString())
            }
        }
    }
}




