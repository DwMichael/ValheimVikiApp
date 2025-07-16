package com.rabbitv.valheimviki.presentation.armor.viewmodel

import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armor_by_id.GetArmorByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armors_by_ids.GetArmorsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armors_by_sub_category_use_case.GetArmorsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_local_armors_use_case.GetLocalArmorsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class ArmorListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var armorUseCases: ArmorUseCases

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var getLocalArmorsUseCase: GetLocalArmorsUseCase

	@Mock
	private lateinit var getArmorByIdUseCase: GetArmorByIdUseCase

	@Mock
	private lateinit var getArmorsByIdsUseCase: GetArmorsByIdsUseCase

	@Mock
	private lateinit var getArmorsBySubCategoryUseCase: GetArmorsBySubCategoryUseCase
	private lateinit var viewModel: ArmorListViewModel

	@BeforeEach
	fun setUp() = runTest {
		Dispatchers.setMain(testDispatcher)

		whenever(getLocalArmorsUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))


		armorUseCases = ArmorUseCases(
			getLocalArmorsUseCase = getLocalArmorsUseCase,
			getArmorByIdUseCase = getArmorByIdUseCase,
			getArmorsByIdsUseCase = getArmorsByIdsUseCase,
			getArmorsBySubCategoryUseCase = getArmorsBySubCategoryUseCase
		)
		viewModel = ArmorListViewModel(armorUseCases, connectivityObserver)
	}


	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun getUiState() {

	}

	@Test
	fun armorListViewModel_ChipSelectedChestArmor_ShouldShowListWithOnlyChestArmor() {
		val chipSelected = ArmorSubCategory.CHEST_ARMOR

		ArmorListViewModel(armorUseCases, connectivityObserver)

		viewModel.onChipSelected(chipSelected)
		assert(viewModel.uiState.value.selectedCategory)
	}

}