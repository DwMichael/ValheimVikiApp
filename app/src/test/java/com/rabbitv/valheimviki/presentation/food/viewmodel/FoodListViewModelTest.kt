package com.rabbitv.valheimviki.presentation.food.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_by_id.GetFoodByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_ids.GetFoodListByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_subCategory.GetFoodListBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_local_food_list.GetLocalFoodListUseCase
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
class FoodListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()


	@Mock
	private lateinit var foodUseCases: FoodUseCases

	@Mock
	private lateinit var getLocalFoodListUseCase: GetLocalFoodListUseCase

	@Mock
	private lateinit var getFoodListByIdsUseCase: GetFoodListByIdsUseCase

	@Mock
	private lateinit var getFoodByIdUseCase: GetFoodByIdUseCase

	@Mock
	private lateinit var getFoodBySubCategoryUseCase: GetFoodListBySubCategoryUseCase

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)

		foodUseCases = FoodUseCases(
			getFoodBySubCategoryUseCase = getFoodBySubCategoryUseCase,
			getFoodListByIdsUseCase = getFoodListByIdsUseCase,
			getFoodByIdUseCase = getFoodByIdUseCase,
			getLocalFoodListUseCase = getLocalFoodListUseCase
		)

	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


}