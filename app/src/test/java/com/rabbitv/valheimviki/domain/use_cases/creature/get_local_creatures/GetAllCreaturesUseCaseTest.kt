//package com.rabbitv.valheimviki.domain.use_cases.creature.get_local_creatures
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
//import org.mockito.Mockito.times
//import org.mockito.Mockito.verify
//import org.mockito.kotlin.any
//import org.mockito.kotlin.never
//import org.mockito.kotlin.whenever
//import kotlin.test.assertEquals
//import kotlin.test.assertFailsWith
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class GetAllCreaturesUseCaseTest {
//    private val testDispatcher = StandardTestDispatcher()
//
//
//    @Mock
//    private lateinit var creatureRepository: CreatureRepository
//    private lateinit var useCase: GetLocalCreaturesUseCase
//
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//
//        creatureRepository = mock()
//        useCase = GetLocalCreaturesUseCase(creatureRepository)
//
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `invoke returns sorted biomes when local data is available`() = runTest(testDispatcher) {
//        // Stub both flows so that none return null.
//        whenever(creatureRepository.getAllCreatures()).thenReturn(flowOf(mockCreaturesLocal))
//        whenever(creatureRepository.getMainBosses()).thenReturn(flowOf(emptyList()))
//
//        whenever(creatureRepository.fetchCreatures("en")).thenReturn(
//            CreatureDto(
//                success = true,
//                error = null,
//                errorDetails = null,
//                creatures = emptyList()
//            )
//        )
//        whenever(creatureRepository.storeCreatures(mockCreaturesApi)).thenReturn(Unit)
//
//        val result = useCase.invoke("en").first()
//        advanceUntilIdle()
//
//        assertEquals(expectedMockLocalCreatures, result, "Expected fetched and sorted creatures")
//        verify(creatureRepository).getAllCreatures()
//        verify(creatureRepository, never()).fetchCreatures("en")
//        verify(creatureRepository, never()).storeCreatures(mockCreaturesLocal)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `invoke fetches remote data when local is empty and returns sorted results`() =
//        runTest(testDispatcher) {
//
//            whenever(creatureRepository.getAllCreatures()).thenReturn(
//                flowOf(emptyList()),
//                flowOf(expectedMockApiCreatures)
//            )
//            whenever(creatureRepository.fetchCreatures("en")).thenReturn(
//                CreatureDto(
//                    success = true,
//                    error = null,
//                    errorDetails = null,
//                    creatures = mockCreaturesApi
//                )
//            )
//            whenever(creatureRepository.storeCreatures(mockCreaturesApi)).thenReturn(Unit)
//            val result = useCase.invoke("en").first()
//
//            advanceUntilIdle()
//
//            assertEquals(
//                expectedMockApiCreatures, result, "Expected fetched and sorted bosses list"
//            )
//
//            verify(creatureRepository, times(2)).getAllCreatures()
//            verify(creatureRepository).fetchCreatures("en")
//            verify(creatureRepository).storeCreatures(mockCreaturesApi)
//        }
//
//
//    @Test
//    fun `invoke throws FetchException when local is empty and remote fetch fails`() =
//        runTest(testDispatcher) {
//            val errorMessage = "No local data available and failed to fetch from internet."
//            val mockEmptyLocalBiomes = flowOf(emptyList<CreatureDtoX>())
//            whenever(creatureRepository.getAllCreatures()).thenReturn(mockEmptyLocalBiomes)
//            whenever(creatureRepository.fetchCreatures("en")).thenThrow(RuntimeException("Remote fetch failed"))
//            val exception = assertFailsWith<FetchException> {
//                useCase.invoke("en").first()
//            }
//
//            assertEquals(errorMessage, exception.message)
//            verify(creatureRepository).getAllCreatures()
//            verify(creatureRepository).fetchCreatures("en")
//            verify(
//                creatureRepository,
//                never()
//            ).storeCreatures(any())
//        }
//}
//
//
