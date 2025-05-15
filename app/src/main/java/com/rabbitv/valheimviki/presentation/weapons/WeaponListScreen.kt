package com.rabbitv.valheimviki.presentation.weapons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ElevatedFilterChip

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PocketKnife
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Sword
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.weapon.WeaponSubCategory
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ShimmerEffect
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDT
import com.rabbitv.valheimviki.ui.theme.YellowDTBorder
import com.rabbitv.valheimviki.ui.theme.YellowDTContainerNotSelected
import com.rabbitv.valheimviki.ui.theme.YellowDTIconColor
import com.rabbitv.valheimviki.ui.theme.YellowDTNotSelected
import com.rabbitv.valheimviki.ui.theme.YellowDTSelected
import com.rabbitv.valheimviki.utils.FakeData

enum class SegmentButtonOptions(val label: String) {
    MELEE("Melee"),
    RANGED("Ranged"),
    MAGIC("Magic"),
    AMMO("Ammo");
    override fun toString() = label
}

@Composable
fun WeaponListScreen(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: WeaponListViewModel = hiltViewModel()
) {
    val weaponListUiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeaponListStateRenderer(
        weaponListUiState = weaponListUiState,
        paddingValues = paddingValues,
        modifier = modifier
        )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponListStateRenderer(
    weaponListUiState: WeaponListUiState,
    paddingValues: PaddingValues,
    modifier: Modifier,
){
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("WeaponListSurface")
            .fillMaxSize()
            .padding(paddingValues)
            .then(modifier)
    ) {

//        when{
//            weaponListUiState.isLoading ||  (weaponListUiState.weaponList.isEmpty() && weaponListUiState.isConnection) ->
//            {
//                ShimmerEffect()
//            }
//
//            (weaponListUiState.error != null || !weaponListUiState.isConnection) && weaponListUiState.weaponList.isEmpty() -> {
//                Box(
//                    modifier = Modifier.testTag("EmptyScreenWeaponList"),
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .testTag("EmptyScreenWeaponList")
//                    ) {
//                        EmptyScreen(
//                            modifier = Modifier.fillMaxSize(),
//                            errorMessage = weaponListUiState.error
//                                ?: "Connect to internet to fetch data"
//                        )
//                    }
//                }
//            }
            WeaponListDisplay(
                weaponListUiState =weaponListUiState
            )

//            else ->{
//
//            }

//        }
    }
}



@Composable
fun WeaponListDisplay(
    weaponListUiState: WeaponListUiState
){

    val items :List<Pair<ImageVector,String>> = listOf(
        Pair(Lucide.Axe,"Axes"),
        Pair( ImageVector.vectorResource(id = R.drawable.club),"Clubs"),
        Pair(Lucide.Sword, "Swords"),
            Pair(ImageVector.vectorResource(id = R.drawable.lance), "Spears"),
                Pair(Lucide.Sword,"Poleartms"),
                    Pair(Lucide.PocketKnife, "Knives"),
                        Pair(Lucide.Grab,"Fists"),
                            Pair(Lucide.Shield,"Shields")
    )

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text("Weapons",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.padding(top = 5.dp , start = BODY_CONTENT_PADDING.dp, end = BODY_CONTENT_PADDING.dp ))
        SegmentedButtonSingleSelect()
        Spacer(Modifier.padding(BODY_CONTENT_PADDING.dp))
        SingleChoiceChipGroup(
            items =  items,
            selectedIndex = 0,
            onSelectedChange = { },
            modifier = Modifier,
        )
    }
}


@Composable
fun SingleChoiceChipGroup(
    items: List<Pair<ImageVector,String>> ,
    selectedIndex: Int ,
    onSelectedChange: (Int) -> Unit ,
    modifier: Modifier = Modifier ,
)
{
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEachIndexed { index, pair, ->
            CustomElevatedFilterChip(
                index = index,
                selectedIndex =selectedIndex,
                onSelectedChange = onSelectedChange,
                label = pair.second,
                icon = pair.first

            )
        }
    }
}

@Composable
fun CustomElevatedFilterChip(
    index:Int,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    label:String,
    icon: ImageVector,
){
   val selectableChipColors = SelectableChipColors(
       containerColor = YellowDTContainerNotSelected,
       labelColor = YellowDTNotSelected,
       leadingIconColor = YellowDTIconColor,
       trailingIconColor = YellowDTIconColor,
       disabledContainerColor = YellowDTContainerNotSelected,
       disabledLabelColor = YellowDTNotSelected,
       disabledLeadingIconColor = YellowDTIconColor,
       disabledTrailingIconColor = YellowDTIconColor,
       selectedContainerColor = YellowDT,
       disabledSelectedContainerColor = YellowDTSelected,
       selectedLabelColor = YellowDTSelected,
       selectedLeadingIconColor = YellowDTSelected,
       selectedTrailingIconColor = YellowDTSelected
   )
    ElevatedFilterChip(
        selected = index == selectedIndex,
        onClick = { onSelectedChange(index) },
        label = { Text(label) },
        colors = selectableChipColors,
        leadingIcon = if (index == selectedIndex) {
            { Icon(Icons.Default.Check, contentDescription = null ,modifier = Modifier.size(FilterChipDefaults.IconSize)) }
        } else
        {
            {Icon(icon, contentDescription = null,modifier = Modifier.size(FilterChipDefaults.IconSize)) }
        }
    )
}


@Composable
fun SegmentedButtonSingleSelect(){
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options : List<SegmentButtonOptions> = SegmentButtonOptions.entries

    val segmentColors = SegmentedButtonDefaults.colors(

        // selected (ON) state
        activeContainerColor = YellowDT,
        activeContentColor   = YellowDTSelected,

        // un-selected (OFF) state
        inactiveContainerColor = YellowDTContainerNotSelected,
        inactiveContentColor   = YellowDTNotSelected,

        // disabled states – optional but nice
        disabledActiveContainerColor   = YellowDT.copy(alpha = 0.38f),
        disabledActiveContentColor     = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
        disabledInactiveContainerColor = Color.Transparent,
        disabledInactiveContentColor   = YellowDT.copy(alpha = 0.38f),
        activeBorderColor = YellowDTBorder,
        inactiveBorderColor = YellowDTBorder,
        disabledActiveBorderColor = YellowDTBorder,
        disabledInactiveBorderColor = YellowDTBorder,
    )
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { selectedIndex = index },
                selected = index == selectedIndex,
                colors = segmentColors
            ) {
                Text(
                    label.toString(),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),

                )
            }
        }
    }
}



@Preview("SingleChoiceChipGroupPreview")
@Composable
fun PreviewSingleChoiceChipGroup(){
    val items :List<Pair<ImageVector,String>> = listOf(
        Pair(Lucide.Axe,"Axes"),
        Pair( ImageVector.vectorResource(id = R.drawable.club),"Clubs"),
        Pair(Lucide.Sword, "Swords"),
        Pair(ImageVector.vectorResource(id = R.drawable.lance), "Spears"),
        Pair(Lucide.Sword,"Poleartms"),
        Pair(Lucide.PocketKnife, "Knives"),
        Pair(Lucide.Grab,"Fists"),
        Pair(Lucide.Shield,"Shields")
    )
    ValheimVikiAppTheme { SingleChoiceChipGroup(
        items = items,
        selectedIndex = 1,
        onSelectedChange = {  },
        modifier = Modifier
    ) }
}

@Preview("CustomElevatedFilterChipSelectedPreview")
@Composable
fun PreviewCustomElevatedFilterChipSelected(){
    ValheimVikiAppTheme {
        CustomElevatedFilterChip(
	        index = 0 ,
	        selectedIndex = 0 ,
	        onSelectedChange = {} ,
	        label = "Axes" ,
	        icon = Lucide.Axe ,
        ) }

}

@Preview("CustomElevatedFilterChipNotSelectedPreview")
@Composable
fun PreviewCustomElevatedFilterChipNotSelected(){
    ValheimVikiAppTheme { CustomElevatedFilterChip(
        index = 1,
        selectedIndex = 0,
        onSelectedChange = {},
        label = "Axes",
        icon = Lucide.Axe
    ) }

}

@Preview("SegmentedButtonSingleSelectPreview")
@Composable
fun PreviewSegmentedButtonSingleSelect(){
    ValheimVikiAppTheme { SegmentedButtonSingleSelect() }

}

@Preview(name = "WeaponListStateRendererPreview")
@Composable
fun PreviewWeaponListStateRenderer() {
    ValheimVikiAppTheme {
        WeaponListStateRenderer(
            weaponListUiState = WeaponListUiState(
                weaponList   = FakeData.fakeWeaponList,
                isConnection = true,
                isLoading    = false,
                error        = null,
            ),
            paddingValues = PaddingValues(),
            Modifier
        )
    }
}

