package com.rabbitv.valheimviki.presentation.weapons.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state.UiCategoryChipState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_local_weapons_use_case.GetLocalWeaponsUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapon_by_id_use_case.GetWeaponByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_ids.GetWeaponsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_sub_category_use_case.GetWeaponsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_sub_type_use_case.GetWeaponsBySubTypeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class WeaponListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var weaponUseCases: WeaponUseCases


	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	lateinit var getLocalWeaponsUseCase: GetLocalWeaponsUseCase

	@Mock
	lateinit var getWeaponByIdUseCase: GetWeaponByIdUseCase

	@Mock
	lateinit var getWeaponsBySubCategoryUseCase: GetWeaponsBySubCategoryUseCase

	@Mock
	lateinit var getWeaponsBySubTypeUseCase: GetWeaponsBySubTypeUseCase

	@Mock
	lateinit var getWeaponsByIdsUseCase: GetWeaponsByIdsUseCase

	private lateinit var viewModel: WeaponListViewModel


	@BeforeTest
	fun setUp() = runTest {
		Dispatchers.setMain(testDispatcher)

		whenever(getLocalWeaponsUseCase()).thenReturn(flowOf(emptyList()))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		weaponUseCases = WeaponUseCases(
			getLocalWeaponsUseCase = getLocalWeaponsUseCase,
			getWeaponByIdUseCase = getWeaponByIdUseCase,
			getWeaponsBySubCategoryUseCase = getWeaponsBySubCategoryUseCase,
			getWeaponsBySubTypeUseCase = getWeaponsBySubTypeUseCase,
			getWeaponsByIdsUseCase = getWeaponsByIdsUseCase,
		)

		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver)
	}

	@AfterTest
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun weaponListViewModel_Initialization_ShouldEmitLoadingState() = runTest {


		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver)

		viewModel.uiState.test {
			assert(awaitItem() is UiCategoryChipState.Loading)
			cancelAndIgnoreRemainingEvents()
		}
	}


	@Test
	fun weaponListViewModel_LoadData_ShouldEmitSuccessState() = runTest {
		val sword = Weapon(
			id = 1.toString(),
			name = "Test Sword",
			subCategory = WeaponSubCategory.MELEE_WEAPON.toString(),
			subType = WeaponSubType.SWORD.toString(),
			order = 0,
			imageUrl = "",
			category = "WEAPON",
			description = "NO DESCRIPTION",
			upgradeInfoList = emptyList(),
		)
		val weapons = listOf(sword)
		whenever(weaponUseCases.getLocalWeaponsUseCase())
			.thenReturn(flowOf(weapons))
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver)

		viewModel.uiState.test {
			assert(awaitItem() is UiCategoryChipState.Loading)
			val success = awaitItem() as UiCategoryChipState.Success
			assertEquals(weapons, success.list)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun weaponListViewModel_NoConnectionAndNoCache_ShouldEmitErrorState() = runTest {

		whenever(connectivityObserver.isConnected)
			.thenReturn(flowOf(false))

		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver)

		viewModel.uiState.test {
			awaitItem()
			val error = awaitItem() as UiCategoryChipState.Error
			assertEquals(
				"No internet connection and no local data available. Try to connect to the internet again.",
				error.message
			)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun weaponListViewModel_NoConnectionAndStoredCache_ShouldEmitSuccessState() = runTest {
		val sword = Weapon(
			id = 1.toString(),
			name = "Test Sword",
			subCategory = WeaponSubCategory.MELEE_WEAPON.toString(),
			subType = WeaponSubType.SWORD.toString(),
			order = 0,
			imageUrl = "",
			category = "WEAPON",
			description = "NO DESCRIPTION",
			upgradeInfoList = emptyList(),
		)
		val weapons = listOf(sword)
		whenever(weaponUseCases.getLocalWeaponsUseCase())
			.thenReturn(flowOf(weapons))
		whenever(connectivityObserver.isConnected)
			.thenReturn(flowOf(false))

		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver)

		viewModel.uiState.test {
			assert(awaitItem() is UiCategoryChipState.Loading)

			val potentialError = awaitItem()
			assert(potentialError is UiCategoryChipState.Error || potentialError is UiCategoryChipState.Success)

			val successState = if (potentialError is UiCategoryChipState.Success) {
				potentialError
			} else {
				awaitItem() as UiCategoryChipState.Success
			}

			assertEquals(weapons, successState.list)
			cancelAndIgnoreRemainingEvents()
		}
	}
}