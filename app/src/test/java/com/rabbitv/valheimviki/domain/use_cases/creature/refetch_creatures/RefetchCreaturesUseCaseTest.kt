package com.rabbitv.valheimviki.domain.use_cases.creature.refetch_creatures

import com.rabbitv.valheimviki.data.Creatures.mockCreaturesApi
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.creature.RefetchUseCases
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.utils.Constants.CreatureTYPE_ORDER_MAP
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class RefetchCreaturesUseCaseTest {
    private val testDispatcher = StandardTestDispatcher()


    @Mock
    private lateinit var creatureRepository: CreatureRepository
    private lateinit var useCase: RefetchCreaturesUseCase


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        creatureRepository = mock()
        useCase = RefetchCreaturesUseCase(creatureRepository)


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should throw FetchException when repository returns an error`() = runTest {
        // Arrange: create a response with an error message.
        val language = "en"
        val errorMessage = "Network error"
        val fetchResponse = CreatureDto(  // Replace with your actual response type.
            success = false,
            error = errorMessage,
            errorDetails = "$errorMessage and not local data",
            creatures = emptyList()
        )
        whenever(creatureRepository.fetchCreatures(language)).thenReturn(fetchResponse)

        // Act & Assert: the use case should throw FetchException.
        val exception = assertFailsWith<FetchException> {
            // The exception is thrown immediately upon calling the use case, before any flow is collected.
            useCase(language, RefetchUseCases.GET_ALL_CREATURES).collect { /* no-op */ }
        }
        assertEquals(errorMessage, exception.message)
    }


    @Test
    fun `invoke should return sorted list for GET_ALL_CREATURES`() = runTest {
        // Arrange
        val language = "en"


        val fetchResponse = CreatureDto(
            true,
            null,
            null,
            creatures = mockCreaturesApi
        )
        whenever(creatureRepository.fetchCreatures(language)).thenReturn(fetchResponse)
        // Simulate getAllCreatures returning the unsorted list.
        whenever(creatureRepository.getAllCreatures()).thenReturn(flowOf(mockCreaturesApi))

        // Act: call the use case.
        val resultFlow = useCase(language, RefetchUseCases.GET_ALL_CREATURES)
        val resultList = resultFlow.first()

        // Assert: verify the proper calls and that the resulting list is sorted.
        verify(creatureRepository).fetchCreatures(language)
        verify(creatureRepository).storeCreatures(mockCreaturesApi)
        verify(creatureRepository).getAllCreatures()

        val expectedList = mockCreaturesApi.sortedWith(
            compareBy<CreatureDtoX> { creature ->
                CreatureTYPE_ORDER_MAP.getOrElse(creature.typeName) { Int.MAX_VALUE }
            }.thenBy { it.order }
        )
        assertEquals(expectedList, resultList)
    }

    @Test
    fun `invoke should return sorted list for GET_BOSSES`() = runTest {
        // Arrange
        val language = "en"

        val fetchResponse = CreatureDto(
            true,
            null,
            null,
            creatures = mockCreaturesApi
        )
        whenever(creatureRepository.fetchCreatures(language)).thenReturn(fetchResponse)
        whenever(creatureRepository.getMainBosses()).thenReturn(flowOf(mockCreaturesApi))

        // Act
        val resultFlow = useCase(language, RefetchUseCases.GET_BOSSES)
        val resultList = resultFlow.first()

        // Assert
        verify(creatureRepository).fetchCreatures(language)
        verify(creatureRepository).storeCreatures(mockCreaturesApi)
        verify(creatureRepository).getMainBosses()

        val expectedList = mockCreaturesApi.sortedWith(
            compareBy<CreatureDtoX> { creature ->
                CreatureTYPE_ORDER_MAP.getOrElse(creature.typeName) { Int.MAX_VALUE }
            }.thenBy { it.order }
        )
        assertEquals(expectedList, resultList)
    }

    @Test
    fun `invoke should return sorted list for GET_MINI_BOSSES`() = runTest {
        // Arrange
        val language = "en"
        val fetchResponse = CreatureDto(
            true,
            null,
            null,
            creatures = mockCreaturesApi
        )
        whenever(creatureRepository.fetchCreatures(language)).thenReturn(fetchResponse)
        whenever(creatureRepository.getMiniBosses()).thenReturn(flowOf(mockCreaturesApi))

        // Act
        val resultFlow = useCase(language, RefetchUseCases.GET_MINI_BOSSES)
        val resultList = resultFlow.first()

        // Assert
        verify(creatureRepository).fetchCreatures(language)
        verify(creatureRepository).storeCreatures(mockCreaturesApi)
        verify(creatureRepository).getMiniBosses()

        val expectedList = mockCreaturesApi.sortedWith(
            compareBy<CreatureDtoX> { creature ->
                CreatureTYPE_ORDER_MAP.getOrElse(creature.typeName) { Int.MAX_VALUE }
            }.thenBy { it.order }
        )
        assertEquals(expectedList, resultList)
    }

}