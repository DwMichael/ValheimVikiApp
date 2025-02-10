package com.rabbitv.valheimviki.presentation.biome


import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes.GetAllBiomesUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BiomeViewModelTest {
    private val mockBiomes = listOf(
        BiomeDtoX(
            id = "biome1-id",
            stage = "Stage 1",
            imageUrl = "https://example.com/biome1.jpg",
            name = "Temperate Forest",
            description = "A forest characterized by moderate rainfall and distinct seasons.",
            order = 1
        ), BiomeDtoX(
            id = "biome2-id",
            stage = "Stage 2",
            imageUrl = "https://example.com/biome2.png",
            name = "Tropical Rainforest",
            description = "A hot, moist biome found near Earth's equator.",
            order = 2
        )
    )

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getAllBiomesUseCase: GetAllBiomesUseCase

    @Mock
    private lateinit var biomeUseCases: BiomeUseCases

    private lateinit var viewModel: BiomeGridScreenViewModel


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Stub the use case invocation:
        whenever(getAllBiomesUseCase.invoke("en"))
            .thenReturn(flowOf(mockBiomes))

        // Stub biomeUseCases.getAllBiomesUseCase to return the mocked getAllBiomesUseCase.
        whenever(biomeUseCases.getAllBiomesUseCase).thenReturn(getAllBiomesUseCase)

        // Create the ViewModel – load() is called in the constructor.
        viewModel = BiomeGridScreenViewModel(biomeUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitializing_updatesBiomeUIStateBeforeLoadAndAfter() = runTest(testDispatcher) {
        val biomeViewModel = BiomeGridScreenViewModel(biomeUseCases)
        val initialState =
            biomeViewModel.biomeUIState.value
        val listBiome: List<BiomeDtoX> = emptyList()
        assertTrue(initialState.isLoading, "Loading should be true")
        assertNull("Error should be null", initialState.error)
        assertEquals("List of Biome is not the same", listBiome, initialState.biomes)

        advanceUntilIdle()

        val finalState = biomeViewModel.biomeUIState.value

        assertFalse("Loading should be false", finalState.isLoading)
        assertNull("Error should be null", finalState.error)
        assertEquals("List of Biome is not the same", mockBiomes, finalState.biomes)
    }

}