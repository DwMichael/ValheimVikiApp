package com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes

import com.rabbitv.valheimviki.domain.model.api_response.ApiResponse
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RefetchBiomesUseCaseTest {
    private val testDispatcher = StandardTestDispatcher()
    private val mockListBiomes =
        listOf(
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


    @Mock
    private lateinit var biomeRepository: BiomeRepository
    private lateinit var useCase: RefetchBiomesUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        biomeRepository = mock()
        useCase = RefetchBiomesUseCase(biomeRepository)


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke returns sorted list of biomes`() = runTest(testDispatcher) {
        whenever(biomeRepository.getAllBiomes()).thenReturn(flowOf(mockListBiomes))
        whenever(biomeRepository.fetchBiomes("en")).thenReturn(
            ApiResponse<Biome>(
                true,
                null,
                null,
                mockListBiomes,
            )
        )
        whenever(biomeRepository.storeBiomes(mockListBiomes)).thenReturn(Unit)

        val expected = listOf(
            Biome(
                id = "biome1-id",
                category = "BIOME",
                imageUrl = "https://example.com/biome1.jpg",
                name = "Temperate Forest",
                description = "A forest characterized by moderate rainfall and distinct seasons.",
                order = 1
            ),Biome(
                id = "biome2-id",
                category = "BIOME",
                imageUrl = "https://example.com/biome2.png",
                name = "Tropical Rainforest",
                description = "A hot, moist biome found near Earth's equator.",
                order = 2
            )
        )
        val result = useCase.invoke("en").first()
        advanceUntilIdle()

        verify(biomeRepository).fetchBiomes("en")
        verify(biomeRepository).storeBiomes(mockListBiomes)
        verify(biomeRepository).getAllBiomes()
        assertEquals(expected, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke throws FetchException when response error is not empty and response success is false`() =
        runTest(testDispatcher) {
            val errorMessage = "No internet connection"
            whenever(biomeRepository.getAllBiomes()).thenReturn(flowOf(mockListBiomes))
            whenever(biomeRepository.fetchBiomes("en")).thenReturn(
                ApiResponse<Biome>(
                    false,
                    errorMessage,
                    "$errorMessage and not local data",
                    mockListBiomes,
                )
            )
            whenever(biomeRepository.storeBiomes(mockListBiomes)).thenReturn(Unit)
            val response = biomeRepository.fetchBiomes("en")
            advanceUntilIdle()

            verify(biomeRepository).fetchBiomes("en")
            verify(
                biomeRepository,
                never()
            ).storeBiomes(any())
            verify(biomeRepository, never()).getAllBiomes()
            assertEquals(errorMessage, response.error)
        }
}