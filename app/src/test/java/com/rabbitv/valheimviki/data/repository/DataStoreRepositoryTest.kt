package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DataStoreRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var dataStore: DataStoreOperations

    private lateinit var repository: DataStoreRepository

    private lateinit var onboardingStateFlow: MutableStateFlow<Boolean>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        onboardingStateFlow = MutableStateFlow(false)

        Mockito.`when`(dataStore.readOnBoardingState()).thenReturn(onboardingStateFlow)



        repository = DataStoreRepository(dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSaveOnBoardingState() = runTest(testDispatcher) {
        Mockito.doAnswer { invocation ->
            val completed = invocation.getArgument<Boolean>(0)
            onboardingStateFlow.value = completed
            null
        }.`when`(dataStore).saveOnBoardingState(Mockito.anyBoolean())

        repository.saveOnBoardingState(true)
        val result = repository.readOnBoardingState().first()

        assertEquals(true, result, "Expected true")
    }

    @Test
    fun testReadOnBoardingState() = runTest(testDispatcher) {
        // This test could initialize the state differently if needed.
        onboardingStateFlow.value = true
        val result = repository.readOnBoardingState().first()
        assertEquals(true, result, "Expected true")
    }

}