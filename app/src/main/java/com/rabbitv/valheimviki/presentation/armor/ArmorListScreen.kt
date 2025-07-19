package com.rabbitv.valheimviki.presentation.armor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Blend
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.armor.ArmorSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.navigation.DetailDestination
import com.rabbitv.valheimviki.navigation.NavigationHelper
import com.rabbitv.valheimviki.presentation.armor.model.ArmorListUiState
import com.rabbitv.valheimviki.presentation.armor.model.ArmorUiEvent
import com.rabbitv.valheimviki.presentation.armor.viewmodel.ArmorListViewModel
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.chip.ChipData
import com.rabbitv.valheimviki.presentation.components.chip.CustomElevatedFilterChip
import com.rabbitv.valheimviki.presentation.components.chip.SearchFilterBar
import com.rabbitv.valheimviki.presentation.components.list.ListContent
import com.rabbitv.valheimviki.presentation.components.shimmering_effect.ShimmerListEffect
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


class ArmorChip(
	override val option: ArmorSubCategory,
	override val icon: ImageVector,
	override val label: String
) : ChipData<ArmorSubCategory>

@Composable
fun ArmorListScreen(
	modifier: Modifier = Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
	paddingValues: PaddingValues,
	viewModel: ArmorListViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	val onChipSelected =
		{ chip: ArmorSubCategory? -> viewModel.onEvent(ArmorUiEvent.ChipSelected(chip)) }

	ArmorListStateRenderer(
		uiState = uiState,
		onChipSelected = onChipSelected,
		paddingValues = paddingValues,
		modifier = modifier,
		onItemClick = onItemClick
	)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArmorListStateRenderer(
	uiState: ArmorListUiState,
	onChipSelected: (ArmorSubCategory?) -> Unit,
	paddingValues: PaddingValues,
	modifier: Modifier,
	onItemClick: (destination: DetailDestination) -> Unit,
) {
	Surface(
		color = Color.Transparent,
		modifier = Modifier
			.testTag("ArmorListSurface")
			.fillMaxSize()
			.padding(paddingValues)
			.then(modifier)
	) {

		ArmorListDisplay(
			uiState = uiState,
			onChipSelected = onChipSelected,
			onItemClick = onItemClick
		)
	}
}


@Composable
fun ArmorListDisplay(
	uiState: ArmorListUiState,
	onChipSelected: (ArmorSubCategory?) -> Unit,
	onItemClick: (destination: DetailDestination) -> Unit,
) {
	val lazyListState = rememberLazyListState()
	val handleItemClick = remember {
		NavigationHelper.createItemDetailClickHandler(onItemClick)
	}
	Column(
		horizontalAlignment = Alignment.Start
	) {
		SearchFilterBar(
			chips = getChipsForCategory(),
			selectedOption = uiState.selectedChip,
			onSelectedChange = { _, subCategory ->
				if (uiState.selectedChip == subCategory) {
					onChipSelected(null)
				} else {
					onChipSelected(subCategory)
				}
			},
			modifier = Modifier,
		)
		Spacer(Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp, vertical = 5.dp))
		when (uiState.armorsUiState) {
			is UIState.Error -> EmptyScreen(errorMessage = stringResource(id = uiState.armorsUiState.message.toInt()))
			is UIState.Loading -> ShimmerListEffect()
			is UIState.Success -> {
				ListContent(
					items = uiState.armorsUiState.data,
					clickToNavigate = handleItemClick,
					lazyListState = lazyListState,
					imageScale = ContentScale.Fit,
					horizontalPadding = 0.dp
				)
			}
		}
	}
}


@Composable
private fun getChipsForCategory(): List<ArmorChip> {
	return listOf(
		ArmorChip(
			ArmorSubCategory.CAPE,
			ImageVector.vectorResource(id = R.drawable.cape),
			"Capes"
		),
		ArmorChip(
			ArmorSubCategory.CHEST_ARMOR,
			ImageVector.vectorResource(id = R.drawable.chest_armor),
			"Chests Armor"
		),
		ArmorChip(
			ArmorSubCategory.LEG_ARMOR,
			ImageVector.vectorResource(id = R.drawable.leg_armor),
			"Legs Armor"
		),
		ArmorChip(
			ArmorSubCategory.ACCESSORIES,
			Lucide.Blend,
			"Accessories"
		),
		ArmorChip(
			ArmorSubCategory.HELMET,
			ImageVector.vectorResource(id = R.drawable.helmet),
			"Helmet"
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
			selectedOption = ArmorSubCategory.CAPE,
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
		ArmorListStateRenderer(
			uiState = ArmorListUiState(
				selectedChip = ArmorSubCategory.CAPE,
				armorsUiState = UIState.Success(
					data = FakeData.fakeArmorList(),
				),
			),
			paddingValues = PaddingValues(),
			modifier = Modifier,
			onChipSelected = {},
			onItemClick = { _ -> {} }
		)
	}
}




