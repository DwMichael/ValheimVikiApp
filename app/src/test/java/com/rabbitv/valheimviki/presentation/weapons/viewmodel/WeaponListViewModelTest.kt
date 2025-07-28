package com.rabbitv.valheimviki.presentation.weapons.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
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
import kotlin.test.fail

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

		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver, testDispatcher)
	}

	@AfterTest
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun weaponListViewModel_Initialization_ShouldEmitLoadingState() = runTest {


		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			assert(awaitItem().weaponUiState is UIState.Loading)
		}
	}


	@Test
	fun weaponListViewModel_uiState_ShouldEmitSuccessState() = runTest {
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

		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver, testDispatcher)
		viewModel.uiState.test {
			assert(awaitItem().weaponUiState is UIState.Loading)

			val result = awaitItem()


			when (result.weaponUiState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assert(result.selectedCategory == WeaponSubCategory.MELEE_WEAPON)
					assertEquals(weapons, result.weaponUiState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun weaponListViewModel_NoConnectionAndNoCache_ShouldEmitErrorState() = runTest {

		whenever(connectivityObserver.isConnected)
			.thenReturn(flowOf(false))

		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver, testDispatcher)

		viewModel.uiState.test {
			assert(awaitItem().weaponUiState is UIState.Loading)

			val state = awaitItem()


			when (state.weaponUiState) {
				is UIState.Error -> {
					assertEquals(
						error_no_connection_with_empty_list_message.toString(),
						state.weaponUiState.message
					)
				}

				is UIState.Loading -> fail("State should have transitioned from Loading to Error")
				is UIState.Success -> fail("State should not emit Success")
			}

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

		viewModel = WeaponListViewModel(weaponUseCases, connectivityObserver, testDispatcher)


		viewModel.uiState.test {
			assert(awaitItem().weaponUiState is UIState.Loading)

			val result = awaitItem()

			when (result.weaponUiState) {
				is UIState.Error -> fail("State should not emit error")
				is UIState.Loading -> fail("State should have transitioned from Loading to Success")
				is UIState.Success -> {
					assert(result.selectedCategory == WeaponSubCategory.MELEE_WEAPON)
					assertEquals(weapons, result.weaponUiState.data)
				}
			}

			cancelAndIgnoreRemainingEvents()
		}
	}
}