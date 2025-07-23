package com.rabbitv.valheimviki.presentation.tool.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.get_local_tools_use_case.GetLocalToolsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tool_by_id.GetToolByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_ids.GetToolsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_sub_category_use_case.GetToolsBySubCategoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class ToolListViewModelTest {
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var getLocalToolsUseCase: GetLocalToolsUseCase

	@Mock
	private lateinit var getToolByIdUseCase: GetToolByIdUseCase

	@Mock
	private lateinit var getToolsByIdsUseCase: GetToolsByIdsUseCase

	@Mock
	private lateinit var getToolsBySubCategoryUseCase: GetToolsBySubCategoryUseCase

	private lateinit var toolUseCases: ToolUseCases

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		toolUseCases = ToolUseCases(
			getLocalToolsUseCase = getLocalToolsUseCase,
			getToolByIdUseCase = getToolByIdUseCase,
			getToolsByIdsUseCase = getToolsByIdsUseCase,
			getToolsBySubCategoryUseCase = getToolsBySubCategoryUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

}