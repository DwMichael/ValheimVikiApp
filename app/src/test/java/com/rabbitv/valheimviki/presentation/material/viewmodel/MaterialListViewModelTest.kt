package com.rabbitv.valheimviki.presentation.material.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.get_local_Materials.GetLocalMaterialsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_material_by_id.GetMaterialByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids.GetMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory.GetMaterialsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory_and_subtype.GetMaterialsBySubCategoryAndSubTypeUseCase
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
class MaterialListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var materialUseCases: MaterialUseCases

	@Mock
	private lateinit var getLocalMaterials: GetLocalMaterialsUseCase

	@Mock
	private lateinit var getMaterialsByIds: GetMaterialsByIdsUseCase

	@Mock
	private lateinit var getMaterialById: GetMaterialByIdUseCase

	@Mock
	private lateinit var getMaterialsBySubCategory: GetMaterialsBySubCategoryUseCase

	@Mock
	private lateinit var getMaterialsBySubCategoryAndSubType: GetMaterialsBySubCategoryAndSubTypeUseCase


	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)

		materialUseCases = MaterialUseCases(
			getLocalMaterials = getLocalMaterials,
			getMaterialsByIds = getMaterialsByIds,
			getMaterialById = getMaterialById,
			getMaterialsBySubCategory = getMaterialsBySubCategory,
			getMaterialsBySubCategoryAndSubType = getMaterialsBySubCategoryAndSubType
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

}