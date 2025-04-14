package com.rabbitv.valheimviki.domain.use_cases.biome.get_local_biomes

import com.rabbitv.valheimviki.domain.exceptions.FetchException
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllBiomesUseCaseTest {
    private val testDispatcher = StandardTestDispatcher()
    private val mockApiBiomes =
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
    private lateinit var useCase: GetLocalBiomesUseCase


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        biomeRepository = mock()
        useCase = GetLocalBiomesUseCase(biomeRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke returns sorted biomes when local data is available`() = runTest(testDispatcher) {
        val mockLocalBiomes = flowOf(
            listOf(
                Biome(
                    id = "biome1-id",
                    category = "BIOME",
                    imageUrl = "https://example.com/biome1.jpg",
                    name = "Temperate Forest local",
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
        )
        whenever(biomeRepository.getAllBiomes()).thenReturn(mockLocalBiomes)
        whenever(biomeRepository.fetchBiomes("en")).thenReturn(
            ApiResponse<Biome>(
                false,
                null,
                null,
                emptyList(),
            )
        )
        whenever(biomeRepository.storeBiomes(mockApiBiomes)).thenReturn(Unit)

        val expected = listOf(
            Biome(
                id = "biome1-id",
                category = "BIOME",
                imageUrl = "https://example.com/biome1.jpg",
                name = "Temperate Forest local",
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
        val result = useCase.invoke("en").first()

        assertEquals(expected, result)
        verify(biomeRepository).getAllBiomes()
        verify(biomeRepository, never()).fetchBiomes("en")
        verify(biomeRepository, never()).storeBiomes(mockApiBiomes)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `invoke fetches remote data when local is empty and returns sorted results`() =
        runTest(testDispatcher) {
            whenever(biomeRepository.getAllBiomes()).thenReturn(
                flowOf(emptyList()),
                flowOf(mockApiBiomes)
            )
            whenever(biomeRepository.fetchBiomes("en")).thenReturn(
                ApiResponse<Biome>(
                    success = true,
                    error = null,
                    message = null,
                    data = mockApiBiomes
                )
            )
            whenever(biomeRepository.storeBiomes(mockApiBiomes)).thenReturn(Unit)
            val expected = listOf(
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
            val result = useCase.invoke("en").first()

            advanceUntilIdle()

            assertEquals(expected, result, "Expected fetched and sorted biomes list")
            verify(biomeRepository, times(2)).getAllBiomes()
            verify(biomeRepository).fetchBiomes("en")
            verify(biomeRepository).storeBiomes(mockApiBiomes)
        }


    @Test
    fun `invoke throws FetchException when local is empty and remote fetch fails`() =
        runTest(testDispatcher) {
            val errorMessage = "No local data available and failed to fetch from internet."
            val mockEmptyLocalBiomes = flowOf(emptyList<Biome>())
            whenever(biomeRepository.getAllBiomes()).thenReturn(mockEmptyLocalBiomes)
            whenever(biomeRepository.fetchBiomes("en")).thenThrow(RuntimeException("Remote fetch failed"))

            val exception = assertFailsWith<FetchException> {
                useCase.invoke("en").first()
            }
            assertEquals(errorMessage, exception.message)

            verify(biomeRepository).getAllBiomes()
            verify(biomeRepository).fetchBiomes("en")
            verify(
                biomeRepository,
                never()
            ).storeBiomes(any())
        }
}

