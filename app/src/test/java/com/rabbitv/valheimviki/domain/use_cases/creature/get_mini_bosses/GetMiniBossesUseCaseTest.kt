//package com.rabbitv.valheimviki.domain.use_cases.creature.get_mini_bosses
//
//import com.rabbitv.valheimviki.domain.exceptions.FetchException
//import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
//import com.rabbitv.valheimviki.domain.repository.CreatureRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.advanceUntilIdle
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.mockito.Mock
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.verify
//import org.mockito.kotlin.any
//import org.mockito.kotlin.never
//import org.mockito.kotlin.whenever
//import kotlin.test.assertEquals
//import kotlin.test.assertFailsWith
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class GetMiniBossesUseCaseTest {
//    private val testDispatcher = StandardTestDispatcher()
//
//
//    @Mock
//    private lateinit var creatureRepository: CreatureRepository
//    private lateinit var useCase: GetMiniBossesUseCase
//
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//
//        creatureRepository = mock()
//        useCase = GetMiniBossesUseCase(creatureRepository)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `invoke returns sorted biomes when local data is available`() = runTest(testDispatcher) {
//
//        whenever(creatureRepository.getMiniBosses()).thenReturn(
//            flowOf(expectedMiniBossList)
//        )
//        whenever(creatureRepository.fetchCreatures("en")).thenReturn(
//            CreatureDto(
//                success = true,
//                error = null,
//                errorDetails = null,
//                creatures = emptyList()
//            )
//        )
//        whenever(creatureRepository.storeCreatures(emptyList())).thenReturn(Unit)
//
//        val result = useCase.invoke("en").first()
//
//        advanceUntilIdle()
//
//        assertEquals(expectedMiniBossList, result, "Expected fetched and sorted creatures")
//        verify(creatureRepository).getMiniBosses()
//        verify(creatureRepository, never()).fetchCreatures("en")
//        verify(creatureRepository, never()).storeCreatures(emptyList())
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `invoke fetches remote data when local is empty and returns sorted results`() = runTest {
//        whenever(creatureRepository.getMiniBosses()).thenReturn(flowOf(emptyList()))
//        whenever(creatureRepository.fetchCreatures("en")).thenReturn(
//            CreatureDto(
//                success = true,
//                error = null,
//                errorDetails = null,
//                creatures = mockCreaturesApi
//            )
//        )
//        whenever(creatureRepository.storeCreatures(mockCreaturesApi)).thenReturn(Unit)
//        whenever(creatureRepository.getAllCreatures()).thenReturn(flowOf(mockCreaturesApi))
//
//        val result = useCase.invoke("en").first()
//        advanceUntilIdle()
//        assertEquals(
//            expectedMockApiCreatures,
//            result,
//            "Expected fetched and sorted creatures list"
//        )
//        verify(creatureRepository).getMiniBosses()
//        verify(creatureRepository).fetchCreatures("en")
//        verify(creatureRepository).storeCreatures(mockCreaturesApi)
//        verify(creatureRepository).getAllCreatures()
//    }
//
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `invoke throws FetchException when local is empty and remote fetch fails`() =
//        runTest(testDispatcher) {
//            val errorMessage = "No local data available and failed to fetch from internet."
//            val emptyLocalList = flowOf(emptyList<CreatureDtoX>())
//            whenever(creatureRepository.getMiniBosses()).thenReturn(emptyLocalList)
//            whenever(creatureRepository.fetchCreatures("en")).thenThrow(RuntimeException("Remote fetch failed"))
//            val exception = assertFailsWith<FetchException> {
//                useCase.invoke("en").first()
//            }
//            assertEquals(errorMessage, exception.message)
//            verify(creatureRepository).getMiniBosses()
//            verify(creatureRepository).fetchCreatures("en")
//            verify(creatureRepository, never()).storeCreatures(any())
//        }
//
//}