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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.CustomElevatedFilterChip
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.presentation.crafting.model.CraftingListUiEvent
import com.rabbitv.valheimviki.presentation.crafting.model.CraftingListUiState
import com.rabbitv.valheimviki.presentation.crafting.viewmodel.CraftingListViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme


data class CraftingChip(
	override val option: CraftingSubCategory,
	override val icon: ImageVector,
	val secondIcon: ImageVector? = null,
	override val label: String
) : ChipData<CraftingSubCategory>

@Composable
fun CraftingListScreen(
	modifier: Modifier = Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
	paddingValues: PaddingValues,
	viewModel: CraftingListViewModel = hiltViewModel()
) {
	val craftingObjectListUiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onChipSelected =
		{ chip: CraftingSubCategory? -> viewModel.onEvent(CraftingListUiEvent.ChipSelected(chip)) }
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
	craftingObjectListUiState: CraftingListUiState,
	onChipSelected: (CraftingSubCategory?) -> Unit,
	paddingValues: PaddingValues,
	modifier: Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
) {
	Surface(
		color = Color.Transparent,
		modifier = Modifier
			.testTag("WeaponListSurface")
			.fillMaxSize()
			.padding(paddingValues)
			.then(modifier)
	) {

		CraftingStationListDisplay(
			craftingObjectListUiState = craftingObjectListUiState,
			onChipSelected = onChipSelected,
			onItemClick = onItemClick
		)
	}
}


@Composable
fun CraftingStationListDisplay(
	craftingObjectListUiState: CraftingListUiState,
	onChipSelected: (CraftingSubCategory?) -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit
) {
	val lazyListState = rememberLazyListState()

	val handleFavoriteItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	Column(
		horizontalAlignment = Alignment.Start
	) {
		SearchFilterBar(
			chips = getChipsForCategory(),
			selectedOption = craftingObjectListUiState.selectedChip,
			onSelectedChange = { _, subCategory -> onChipSelected(subCategory) },
			modifier = Modifier,
		)
		Spacer(Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 5.dp))
		when (val state = craftingObjectListUiState.craftingListUiState) {
			is UIState.Error -> EmptyScreen(errorMessage = state.message)
			is UIState.Loading -> ShimmerListEffect()
			is UIState.Success ->
				ListContent(
					items = state.data,
					clickToNavigate = handleFavoriteItemClick,
					lazyListState = lazyListState,
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
			onSelectedChange = { _,_-> },
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
			onSelectedChange = { _,_-> },
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
			onSelectedChange = { _,_-> },
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
			craftingObjectListUiState = CraftingListUiState(
				selectedChip = null,
				craftingListUiState = UIState.Loading
			),
			paddingValues = PaddingValues(),
			modifier = Modifier,
			onChipSelected = {},
			onItemClick = { _ ->  }
		)
	}
}




