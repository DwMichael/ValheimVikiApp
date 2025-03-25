package com.rabbitv.valheimviki.presentation.biome


import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes.GetAllBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes.RefetchBiomesUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
        Biome(
            id = "biome1-id",
            category = "BIOME",
            imageUrl = "https://example.com/biome1.jpg",
            name = "Temperate Forest",
            description = "A forest characterized by moderate rainfall and distinct seasons.",
            order = 1
        ), Biome(
            id = "biome2-id",
            category = "BIOME",
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
    private lateinit var refetchBiomesUseCase: RefetchBiomesUseCase

    @Mock
    private lateinit var biomeUseCases: BiomeUseCases

    private lateinit var viewModel: BiomeScreenViewModel


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        whenever(getAllBiomesUseCase.invoke("en"))
            .thenReturn(flowOf(mockBiomes))

        whenever(biomeUseCases.getAllBiomesUseCase).thenReturn(getAllBiomesUseCase)

        whenever(biomeUseCases.refetchBiomesUseCase).thenReturn(refetchBiomesUseCase)
        viewModel = BiomeScreenViewModel(biomeUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun testInitializing_updatesBiomeUIStateBeforeLoadAndAfter() = runTest(testDispatcher) {
        val biomeViewModel = BiomeScreenViewModel(biomeUseCases)
        val initialState =
            biomeViewModel.biomeUIState.value
        val listBiome: List<Biome> = emptyList()
        assertTrue(initialState.isLoading, "Loading should be true")
        assertNull("Error should be null", initialState.error)
        assertEquals("List of Biome is not the same", listBiome, initialState.biomes)

        advanceUntilIdle()

        val finalState = biomeViewModel.biomeUIState.value

        assertFalse("Loading should be false", finalState.isLoading)
        assertNull("Error should be null", finalState.error)
        assertEquals("List of Biome is not the same", mockBiomes, finalState.biomes)
    }


    @Test
    fun wrongInitializationImpactsUiState() = runTest(testDispatcher) {
        val biomeViewModel = BiomeScreenViewModel(biomeUseCases)
        val initialState =
            biomeViewModel.biomeUIState.value
        val listBiome: List<Biome> = emptyList()
        val errorMessage = "No local data available and failed to fetch from internet."
        whenever(getAllBiomesUseCase.invoke("en")).thenReturn(flow {
            throw FetchException(
                errorMessage
            )
        })

        assertTrue(initialState.isLoading, "Loading should be true")
        assertNull("Error should be null", initialState.error)
        assertEquals("List of Biome is not the same", listBiome, initialState.biomes)

        advanceUntilIdle()


        val finalState = biomeViewModel.biomeUIState.value

        assertFalse("Loading should be false in final state after error", finalState.isLoading)
        assertEquals(errorMessage, finalState.error)
        assertEquals("List of Biome is still empty after error", listBiome, finalState.biomes)

    }


    @Test
    fun testRefetchingBiomes_updatesBiomeUIStateBeforeAndAfter() = runTest(testDispatcher) {
        val viewModel = BiomeScreenViewModel(biomeUseCases)
        val initialUiState =
            viewModel.biomeUIState.value
        val emptyBiomes: List<Biome> = emptyList()
        whenever(refetchBiomesUseCase.invoke("en"))
            .thenReturn(flowOf(mockBiomes))
        advanceUntilIdle()

        assertTrue(initialUiState.isLoading, "Loading should be true")
        assertNull("Error should be null", initialUiState.error)
        assertEquals("List of Biome is not the same", emptyBiomes, initialUiState.biomes)


        val uiStateAfterInitialFetch = viewModel.biomeUIState.value

        assertFalse("Loading should be false", uiStateAfterInitialFetch.isLoading)
        assertNull("Error should be null", uiStateAfterInitialFetch.error)
        assertEquals("List of Biome is not the same", mockBiomes, uiStateAfterInitialFetch.biomes)



        viewModel.refetchBiomes()

        val whenRefreshSate =
            viewModel.biomeUIState.value

        assertTrue(whenRefreshSate.isLoading, "Loading should be true")
        assertNull("Error should be null", whenRefreshSate.error)
        assertEquals("List of Biome is not the same", mockBiomes, whenRefreshSate.biomes)

        advanceUntilIdle()

        val finalUiState = viewModel.biomeUIState.value

        assertFalse("Loading should be false", finalUiState.isLoading)
        assertNull("Error should be null", finalUiState.error)
        assertEquals("List of Biome is not the same", mockBiomes, finalUiState.biomes)
    }


    @Test
    fun wrongRefetchingBiomesImpactsUiState() = runTest {
        whenever(biomeUseCases.getAllBiomesUseCase("en")).thenReturn(flowOf(mockBiomes))

        val errorMessage = "No local data available and failed to fetch from internet."
        whenever(biomeUseCases.refetchBiomesUseCase("en")).thenReturn(flow {
            throw FetchException(errorMessage)
        })

        val viewModel = BiomeScreenViewModel(biomeUseCases)

        advanceUntilIdle()

        val initialState = viewModel.biomeUIState.value
        assertFalse("Initial load: Loading should be false", initialState.isLoading)
        assertNull(initialState.error)
        assertEquals("Initial list of biomes should match", mockBiomes, initialState.biomes)

        viewModel.refetchBiomes()

        val duringState = viewModel.biomeUIState.value
        assertTrue(duringState.isLoading, "During refetch, loading should be true")
        assertNull(duringState.error)
        assertEquals(
            "List of biomes should remain unchanged during refetch",
            mockBiomes,
            duringState.biomes
        )

        advanceUntilIdle()

        val finalState = viewModel.biomeUIState.value
        assertFalse("After refetch, loading should be false", finalState.isLoading)
        assertEquals(errorMessage, finalState.error)
        assertEquals(
            "List of biomes should remain unchanged after refetch error",
            mockBiomes,
            finalState.biomes
        )
    }


}