package com.rabbitv.valheimviki.presentation.tree.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.get_local_trees.GetLocalTreesUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_tree_by_id.GetTreeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_trees_by_ids.GetTreesByIdsUseCase
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
class TreeScreenViewModelTest {
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var getLocalTreesUseCase: GetLocalTreesUseCase

	@Mock
	private lateinit var getTreeByIdUseCase: GetTreeByIdUseCase

	@Mock
	private lateinit var getTreesByIdsUseCase: GetTreesByIdsUseCase

	private lateinit var treeUseCases: TreeUseCases

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		treeUseCases = TreeUseCases(
			getLocalTreesUseCase = getLocalTreesUseCase,
			getTreeByIdUseCase = getTreeByIdUseCase,
			getTreesByIdsUseCase = getTreesByIdsUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


}