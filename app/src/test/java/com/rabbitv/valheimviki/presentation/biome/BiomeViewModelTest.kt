package com.rabbitv.valheimviki.presentation.biome

import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes.GetAllBiomesUseCase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class BiomeViewModelTest{

	@Mock
	private lateinit var viewModel: BiomeGridScreenViewModel
	private lateinit var biomeUseCases: BiomeUseCases
	private lateinit var getAllBiomesUseCase: GetAllBiomesUseCase

	private val testDispatcher = StandardTestDispatcher()

	@Test
	fun `isRefreshing - should initialize with false`(){
		val viewModel = BiomeGridScreenViewModel(biomeUseCases = biomeUseCases)
		assertEquals(false,viewModel.isRefreshing.value)
	}

	@Test
	fun `biomeUIState - should initialize with BiomeUIState(emptyList,null,false)`(){
		val viewModel = BiomeGridScreenViewModel(biomeUseCases = biomeUseCases)
		assertEquals(BiomesUIState(emptyList<BiomeDtoX>() ,null,true),viewModel.biomeUIState.value)
	}

	@Test
	fun `load() sets isLoading to true initially and false after success`() = runTest(testDispatcher) {
		val mockBiomes:List<BiomeDtoX> = listOf(BiomeDtoX(
			id = "biome1-id",
			stage = "Stage 1",
			imageUrl = "https://example.com/biome1.jpg",
			name = "Temperate Forest",
			description = "A forest characterized by moderate rainfall and distinct seasons.",
			order = 1
		),BiomeDtoX(
			id = "biome2-id",
			stage = "Stage 2",
			imageUrl = "https://example.com/biome2.png",
			name = "Tropical Rainforest",
			description = "A hot, moist biome found near Earth's equator.",
			order = 2
		))
		`when`(getAllBiomesUseCase.invoke("en")).thenReturn(kotlinx.coroutines.flow.flowOf(mockBiomes)) // Mock success

		assertEquals(true,viewModel.biomeUIState.value.isLoading)


		advanceUntilIdle()

		assertEquals(false,viewModel.biomeUIState.value.isLoading)
		assertEquals(mockBiomes,viewModel.biomeUIState.value.biomes)

	}

}