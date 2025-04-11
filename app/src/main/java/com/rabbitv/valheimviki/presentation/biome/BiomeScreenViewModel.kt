package com.rabbitv.valheimviki.presentation.biome

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.exceptions.BiomeFetchException
import com.rabbitv.valheimviki.domain.exceptions.BiomesInsertException
import com.rabbitv.valheimviki.domain.exceptions.CreatureFetchException
import com.rabbitv.valheimviki.domain.exceptions.CreaturesInsertException
import com.rabbitv.valheimviki.domain.exceptions.FetchAndInsertException
import com.rabbitv.valheimviki.domain.exceptions.RelationFetchException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

data class BiomesUIState(
    val biomes: List<Biome> = emptyList(),
    val areCreatures: Boolean = false,
    val areRelations: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class BiomeScreenViewModel @Inject constructor(
    private val relationsRepository: RelationUseCases,
    private val creatureUseCases: CreatureUseCases,
    private val biomeUseCases: BiomeUseCases,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _biomeUIState = MutableStateFlow(BiomesUIState())
    val biomeUIState: StateFlow<BiomesUIState> = _biomeUIState

    init {
        load()
    }

    //    _biomeUIState.update { current ->
//        current.copy(biomes = sortedBiomes, isLoading = false)
//    }
    @VisibleForTesting
    internal fun load() {
        _biomeUIState.value = _biomeUIState.value.copy(isLoading = true, error = null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                biomeUseCases.getAllBiomesUseCase("en").collect { sortedBiomes ->
                    _biomeUIState.update { current ->
                        current.copy(biomes = sortedBiomes,isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                creatureUseCases.fetchCreatureAndInsertUseCase("en")
                _biomeUIState.value = _biomeUIState.value.copy(areCreatures = true)
            }catch(e: CreatureFetchException){
                Log.e("CreatureFetchException", "${e.message}")
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
            catch (e: CreaturesInsertException) {
                Log.e("CreaturesInsertException", "${e.message}")
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
            catch (e: FetchAndInsertException)
            {
                Log.e("FetchAndInsertException", "${e.message}")
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
            catch (e: Exception) {
                Log.e("Unexpected Exception occurred", "${e.message}")
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                relationsRepository.fetchAndInsertRelationsUseCase()
                _biomeUIState.value = _biomeUIState.value.copy(areRelations = true)
            } catch (e: Exception) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
        }

    }

    fun refetchBiomes() {
        _biomeUIState.value = _biomeUIState.value.copy(isLoading = true, error = null)
        viewModelScope.launch(Dispatchers.IO) {
            if (!isNetworkAvailable(context)) {
                _biomeUIState.value = _biomeUIState.value.copy(
                    isLoading = false, error = "No internet connection"
                )
                _isRefreshing.emit(false)
                return@launch
            }

            try {
                biomeUseCases.refetchBiomesUseCase("en").collect { sortedBiomes ->
                    _biomeUIState.update { current ->
                        current.copy(biomes = sortedBiomes, isLoading = false)
                    }
                }
            }catch (e:BiomeFetchException)
            {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
            catch (e: BiomesInsertException)
            {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
            catch (e: RelationFetchException) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            }
            catch (e: Exception) {
                _biomeUIState.value = _biomeUIState.value.copy(isLoading = false, error = e.message)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }
}