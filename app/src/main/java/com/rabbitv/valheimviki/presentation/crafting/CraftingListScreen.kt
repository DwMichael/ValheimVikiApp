package com.rabbitv.valheimviki.presentation.crafting

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Apple
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.ChefHat
import com.composables.icons.lucide.Cog
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.TrendingUp
import com.composables.icons.lucide.Wrench
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.category_state.UiCategoryState
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.list.ListContent

import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.CustomElevatedFilterChip
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.crafting.viewmodel.CraftingListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


data class CraftingChip(
	override val option: CraftingSubCategory,
	override val icon: ImageVector,
	val secondIcon: ImageVector? = null,
	override val label: String
) : ChipData<CraftingSubCategory>

@Composable
fun CraftingListScreen(
	modifier: Modifier = Modifier,
	onItemClick: (armorId: String, _: Int) -> Unit,
	paddingValues: PaddingValues,
	viewModel: CraftingListViewModel = hiltViewModel()
) {
	val craftingObjectListUiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onChipSelected = { chip: CraftingSubCategory? -> viewModel.onChipSelected(chip) }
	CraftingListStateRenderer(
		craftingObjectListUiState = craftingObjectListUiState,
		onChipSelected = onChipSelected,
		paddingValues = paddingValues,
		modifier = modifier,
		onItemClick = onItemClick
	)

}

@Composable
fun CraftingListStateRenderer(
	craftingObjectListUiState: UiCategoryState<CraftingSubCategory?, CraftingObject>,
	onChipSelected: (CraftingSubCategory?) -> Unit,
	paddingValues: PaddingValues,
	modifier: Modifier,
	onItemClick: (armorId: String, _: Int) -> Unit,
) {
	Surface(
		color = Color.Transparent,
		modifier = Modifier
            .testTag("WeaponListSurface")
            .fillMaxSize()
            .padding(paddingValues)
            .then(modifier)
	) {

		ArmorListDisplay(
			craftingObjectListUiState = craftingObjectListUiState,
			onChipSelected = onChipSelected,
			onItemClick = onItemClick
		)
	}
}


@Composable
fun ArmorListDisplay(
	craftingObjectListUiState: UiCategoryState<CraftingSubCategory?, CraftingObject>,
	onChipSelected: (CraftingSubCategory?) -> Unit,
	onItemClick: (armorId: String, _: Int) -> Unit
) {


	val lazyListState = rememberLazyListState()




	Column(
		horizontalAlignment = Alignment.Start
	) {
		SearchFilterBar(
			chips = getChipsForCategory(),
			selectedOption = craftingObjectListUiState.selectedCategory,
			onSelectedChange = { _, subCategory ->
				if (craftingObjectListUiState.selectedCategory == subCategory) {
					onChipSelected(null)
				} else {
					onChipSelected(subCategory)
				}
			},
			modifier = Modifier,
		)
		Spacer(Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 5.dp))
		when (val state = craftingObjectListUiState) {
			is UiCategoryState.Error<CraftingSubCategory?> -> EmptyScreen(errorMessage = state.message.toString())
			is UiCategoryState.Loading<CraftingSubCategory?> -> ShimmerListEffect()
			is UiCategoryState.Success<CraftingSubCategory?, CraftingObject> ->
				ListContent(
				items = state.list,
				clickToNavigate = onItemClick,
				lazyListState = lazyListState,
				subCategoryNumber = 0,
				imageScale = ContentScale.Fit,
				horizontalPadding = 0.dp
			)
		}
	}
}


@Composable
private fun getChipsForCategory(): List<CraftingChip> {
	return listOf(
		CraftingChip(
			CraftingSubCategory.CRAFTING_STATION,
			Lucide.Wrench,
			label = "Crafting Stations"
		),
		CraftingChip(
			CraftingSubCategory.FOOD_CRAFTING,
			Lucide.ChefHat,
			label = "Food crafting Stations"
		),
		CraftingChip(
			CraftingSubCategory.SMELTING_CRAFTING,
			Lucide.Flame,
			label = "Smelting Stations"
		),
		CraftingChip(
			CraftingSubCategory.REFINING_STATION,
			Lucide.Cog,
			label = "Refinery Stations"
		),
		CraftingChip(
			CraftingSubCategory.CRAFTING_UPGRADER,
			Lucide.TrendingUp,
			label = "Crafting Upgraders"
		),
		CraftingChip(
			CraftingSubCategory.CRAFTING_UPGRADER_FOOD,
			Lucide.Apple,
			Lucide.TrendingUp,
			label = "Crafting Upgraders For Food Station"
		),
	)
}


@Preview("SingleChoiceChipPreview")
@Composable
fun PreviewSingleChoiceChip() {
	ValheimVikiAppTheme {
		SearchFilterBar(
			onSelectedChange = { i, s -> {} },
			modifier = Modifier,
			selectedOption = CraftingSubCategory.CRAFTING_STATION,
			chips = getChipsForCategory()
		)
	}
}


@Preview("CustomElevatedFilterChipSelectedPreview")
@Composable
fun PreviewCustomElevatedFilterChipSelected() {
	ValheimVikiAppTheme {
		CustomElevatedFilterChip(

			index = 0,
			selectedChipIndex = 0,
			onSelectedChange = { i, s -> {} },
			label = "Axes",
			icon = Lucide.Axe,
			option = ArmorSubCategory.CAPE,
		)
	}

}

@Preview("CustomElevatedFilterChipNotSelectedPreview")
@Composable
fun PreviewCustomElevatedFilterChipNotSelected() {
	ValheimVikiAppTheme {
		CustomElevatedFilterChip(
			index = 1,
			selectedChipIndex = 0,
			onSelectedChange = { i, s -> {} },
			label = "Axes",
			icon = Lucide.Axe,
			option = ArmorSubCategory.CAPE,
		)
	}

}


@Preview(name = "WeaponListStateRendererPreview")
@Composable
fun PreviewWeaponListStateRenderer() {
	ValheimVikiAppTheme {
		CraftingListStateRenderer(
			craftingObjectListUiState = UiCategoryState.Success(
				selectedCategory = CraftingSubCategory.CRAFTING_STATION,
				list = FakeData.fakeCraftingObjectList()
			),
			paddingValues = PaddingValues(),
			modifier = Modifier,
			onChipSelected = {},
			onItemClick = { _, _ -> {} }
		)
	}
}




