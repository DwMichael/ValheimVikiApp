package com.rabbitv.valheimviki.presentation.weapons

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Bomb
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Wand
import com.composables.icons.lucide.WandSparkles
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.ui_state.category_chip_state.UiCategoryChipState
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubType
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.segmented.SegmentedButtonSingleSelect
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.weapons.model.WeaponSegmentOption
import com.rabbitv.valheimviki.presentation.weapons.viewmodel.WeaponListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


data class WeaponChip(
	override val option: WeaponSubType,
	override val icon: ImageVector,
	override val label: String
) : ChipData<WeaponSubType>

@Composable
fun WeaponListScreen(
	modifier: Modifier = Modifier,
	onItemClick: (weaponId: String, _: Int) -> Unit,
	paddingValues: PaddingValues,
	viewModel: WeaponListViewModel = hiltViewModel()
) {
	val weaponListUiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onCategorySelected = { cat: WeaponSubCategory -> viewModel.onCategorySelected(cat) }
	val onChipSelected = { chip: WeaponSubType? -> viewModel.onChipSelected(chip) }
	WeaponListStateRenderer(
		weaponListUiState = weaponListUiState,
		onCategorySelected = onCategorySelected,
		onChipSelected = onChipSelected,
		paddingValues = paddingValues,
		modifier = modifier,
		onItemClick = onItemClick
	)

}


@Composable
fun WeaponListStateRenderer(
	weaponListUiState: UiCategoryChipState<WeaponSubCategory, WeaponSubType?, Weapon>,
	onCategorySelected: (WeaponSubCategory) -> Unit,
	onChipSelected: (WeaponSubType?) -> Unit,
	paddingValues: PaddingValues,
	modifier: Modifier,
	onItemClick: (weaponId: String, _: Int) -> Unit,
) {
	Surface(
		color = Color.Transparent,
		modifier = Modifier
			.testTag("WeaponListSurface")
			.fillMaxSize()
			.padding(paddingValues)
			.then(modifier),

		) {
		WeaponListDisplay(
			weaponListUiState = weaponListUiState,
			onCategorySelected = onCategorySelected,
			onChipSelected = onChipSelected,
			onItemClick = onItemClick
		)
	}
}


@Composable
fun WeaponListDisplay(
	weaponListUiState: UiCategoryChipState<WeaponSubCategory, WeaponSubType?, Weapon>,
	onCategorySelected: (WeaponSubCategory) -> Unit,
	onChipSelected: (WeaponSubType?) -> Unit,
	onItemClick: (weaponId: String, _: Int) -> Unit,
) {
	val lazyListState = rememberLazyListState()

	val selectedChipIndex = getWeaponCategoryIndex(category = weaponListUiState.selectedCategory)

	Column(
		horizontalAlignment = Alignment.Start
	) {
		SegmentedButtonSingleSelect(
			options = WeaponSegmentOption.entries,
			selectedOption = weaponListUiState.selectedCategory,
			onOptionSelected = { index ->
				onCategorySelected(index)
				onChipSelected(null)
			}
		)
		Spacer(Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 5.dp))

		SearchFilterBar(
			chips = getChipsForCategory(weaponListUiState.selectedCategory),
			selectedOption = weaponListUiState.selectedChip,
			onSelectedChange = { _, subType ->
				if (weaponListUiState.selectedChip == subType) {
					onChipSelected(null)
				} else {
					onChipSelected(subType)
				}
			},
			modifier = Modifier
		)


		Spacer(Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 5.dp))
		when (val state = weaponListUiState) {
			is UiCategoryChipState.Loading<WeaponSubCategory, WeaponSubType?> -> ShimmerListEffect()

			is UiCategoryChipState.Error<WeaponSubCategory, WeaponSubType?> -> {
				Log.e("WeaponListScreen", "Error: ${state.message}")
				Box(
					modifier = Modifier.testTag("EmptyScreenWeaponList"),
				) {
					Box(
						modifier = Modifier
							.fillMaxSize()
							.testTag("EmptyScreenWeaponList")
					) {
						EmptyScreen(
							errorMessage = state.message
						)
					}
				}
			}

			is UiCategoryChipState.Success<WeaponSubCategory, WeaponSubType?, Weapon> -> {
				ListContent(
					items = state.list,
					clickToNavigate = onItemClick,
					lazyListState = lazyListState,
					subCategoryNumber = selectedChipIndex,
					imageScale = ContentScale.Fit,
					horizontalPadding = 0.dp
				)
			}
		}
	}
}


@Composable
private fun getChipsForCategory(category: WeaponSubCategory): List<WeaponChip> {
	return when (category) {
		WeaponSubCategory.MELEE_WEAPON -> listOf(
			WeaponChip(WeaponSubType.AXE, Lucide.Axe, "Axes"),
			WeaponChip(
				WeaponSubType.CLUB,
				ImageVector.vectorResource(id = R.drawable.club),
				"Clubs"
			),
			WeaponChip(
				WeaponSubType.SWORD,
				ImageVector.vectorResource(id = R.drawable.sword),
				"Swords"
			),
			WeaponChip(
				WeaponSubType.SPEAR,
				ImageVector.vectorResource(id = R.drawable.spear),
				"Spears"
			),
			WeaponChip(
				WeaponSubType.POLEARM,
				ImageVector.vectorResource(id = R.drawable.polearm),
				"Polearms"
			), // Fixed typo
			WeaponChip(
				WeaponSubType.KNIFES,
				ImageVector.vectorResource(id = R.drawable.knife),
				"Knives"
			),
			WeaponChip(WeaponSubType.FISTS, Lucide.Grab, "Fists"),
			WeaponChip(WeaponSubType.SHIELD, Lucide.Shield, "Shields")
		)

		WeaponSubCategory.RANGED_WEAPON -> listOf(
			WeaponChip(
				WeaponSubType.BOW,
				ImageVector.vectorResource(id = R.drawable.bow_arrow),
				"Bows"
			),
			WeaponChip(
				WeaponSubType.CROSSBOW,
				ImageVector.vectorResource(id = R.drawable.crossbow),
				"Crossbows"
			)
		)

		WeaponSubCategory.MAGIC_WEAPON -> listOf(
			WeaponChip(WeaponSubType.ELEMENTAL_MAGIC, Lucide.WandSparkles, "Elemental magic"),
			WeaponChip(WeaponSubType.BLOOD_MAGIC, Lucide.Wand, "Blood magic")
		)

		WeaponSubCategory.AMMO -> listOf(
			WeaponChip(
				WeaponSubType.ARROW,
				ImageVector.vectorResource(id = R.drawable.arrow),
				"Arrows"
			),
			WeaponChip(
				WeaponSubType.BOLT,
				ImageVector.vectorResource(id = R.drawable.bolt),
				"Bolts"
			), // Fixed label
			WeaponChip(
				WeaponSubType.MISSILE,
				ImageVector.vectorResource(id = R.drawable.missile),
				"Missiles"
			),
			WeaponChip(WeaponSubType.BOMB, Lucide.Bomb, "Bombs")
		)
	}
}


@Preview("SingleChoiceChipMeleePreview")
@Composable
fun PreviewSingleChoiceChipMelee() {
	ValheimVikiAppTheme {
		SearchFilterBar(
			chips = getChipsForCategory(WeaponSubCategory.MELEE_WEAPON),
			selectedOption = WeaponSubType.SWORD,
			onSelectedChange = { i, s -> {} },
			modifier = Modifier,
		)
	}
}

@Preview("SingleChoiceChipRangedPreview")
@Composable
fun PreviewSingleChoiceChipRanged() {


	ValheimVikiAppTheme {
		SearchFilterBar(
			chips = getChipsForCategory(WeaponSubCategory.RANGED_WEAPON),
			selectedOption = WeaponSubType.BOW,
			onSelectedChange = { i, s -> {} },
			modifier = Modifier,
		)
	}
}

@Preview("SingleChoiceChipMagicPreview")
@Composable
fun PreviewSingleChoiceChipMagic() {

	ValheimVikiAppTheme {
		SearchFilterBar(
			chips = getChipsForCategory(WeaponSubCategory.MAGIC_WEAPON),
			selectedOption = WeaponSubType.ELEMENTAL_MAGIC,
			onSelectedChange = { i, s -> {} },
			modifier = Modifier,
		)
	}
}

@Preview("SingleChoiceChipAmmoPreview")
@Composable
fun PreviewSingleChoiceChipAmmo() {

	ValheimVikiAppTheme {
		SearchFilterBar(
			selectedOption = WeaponSubType.BOMB,
			onSelectedChange = { i, s -> {} },
			chips = getChipsForCategory(WeaponSubCategory.AMMO),
			modifier = Modifier,
		)
	}
}


@Preview(name = "WeaponListStateRendererPreview")
@Composable
fun PreviewWeaponListStateRenderer() {

	ValheimVikiAppTheme {
		WeaponListStateRenderer(
			weaponListUiState = UiCategoryChipState.Success(
				selectedCategory = WeaponSubCategory.MELEE_WEAPON,
				selectedChip = WeaponSubType.SWORD,
				list = FakeData.fakeWeaponList,
			),
			paddingValues = PaddingValues(),
			modifier = Modifier,
			onCategorySelected = {},
			onChipSelected = {},
			onItemClick = { _, _ -> {} },
		)
	}
}


private fun getWeaponCategoryIndex(category: WeaponSubCategory): Int {
	return when (category) {
		WeaponSubCategory.MELEE_WEAPON -> 0
		WeaponSubCategory.RANGED_WEAPON -> 1
		WeaponSubCategory.MAGIC_WEAPON -> 2
		WeaponSubCategory.AMMO -> 3
	}
}

private fun getWeaponCategory(index: Int): WeaponSubCategory {
	return when (index) {
		0 -> WeaponSubCategory.MELEE_WEAPON
		1 -> WeaponSubCategory.RANGED_WEAPON
		2 -> WeaponSubCategory.MAGIC_WEAPON
		else -> WeaponSubCategory.AMMO
	}
}
