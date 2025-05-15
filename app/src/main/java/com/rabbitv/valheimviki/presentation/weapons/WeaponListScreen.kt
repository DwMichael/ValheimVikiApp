package com.rabbitv.valheimviki.presentation.weapons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Axe
import com.composables.icons.lucide.Bomb
import com.composables.icons.lucide.Grab
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PocketKnife
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Slice
import com.composables.icons.lucide.Sword
import com.composables.icons.lucide.Swords
import com.composables.icons.lucide.TypeOutline
import com.composables.icons.lucide.Wand
import com.composables.icons.lucide.WandSparkles
import com.rabbitv.valheimviki.R

import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDT
import com.rabbitv.valheimviki.ui.theme.YellowDTBorder
import com.rabbitv.valheimviki.ui.theme.YellowDTContainerNotSelected
import com.rabbitv.valheimviki.ui.theme.YellowDTIconColor
import com.rabbitv.valheimviki.ui.theme.YellowDTNotSelected
import com.rabbitv.valheimviki.ui.theme.YellowDTSelected
import com.rabbitv.valheimviki.utils.FakeData
import kotlin.Int

enum class SegmentButtonOptions(val label: String) {
    MELEE("Melee"),
    RANGED("Ranged"),
    MAGIC("Magic"),
    AMMO("Ammo");
}

data class ChoiceChip(
    val icon: ImageVector,
    val label: String                 // or @StringRes val label: Int
)

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

    val meleeItems :List<ChoiceChip> = listOf(
        ChoiceChip(Lucide.Axe,"Axes"),
        ChoiceChip( ImageVector.vectorResource(id = R.drawable.club),"Clubs"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.sword), "Swords"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.spear), "Spears"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.polearm),"Poleartms"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.knife), "Knives"),
        ChoiceChip(Lucide.Grab,"Fists"),
        ChoiceChip(Lucide.Shield,"Shields")
    )
    val rangedItems :List<ChoiceChip> = listOf(
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.bow_arrow),"Bows"),
        ChoiceChip( ImageVector.vectorResource(id = R.drawable.crossbow),"Crossbows"),
    )
    val magicItems :List<ChoiceChip> = listOf(
        ChoiceChip(Lucide.WandSparkles,"Elemental magic"),
        ChoiceChip(Lucide.Wand,"Blood magic")
    )
    val ammoItems :List<ChoiceChip> = listOf(
        ChoiceChip( ImageVector.vectorResource(id = R.drawable.arrow),"Arrows"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.bolt),"Arrows"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.missile),"Missiles"),
        ChoiceChip(Lucide.Bomb,"Bombs"),
    )
    val combineIcons : Map<Int, List<ChoiceChip>>  = mapOf(
       0 to meleeItems,
       1 to rangedItems,
       2 to magicItems,
       3 to  ammoItems
    )

    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var selectedChipIndex :Int? by remember { mutableStateOf(null) }


    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text("Weapons",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(Modifier.padding(top = 5.dp , start = BODY_CONTENT_PADDING.dp, end = BODY_CONTENT_PADDING.dp ))
        SegmentedButtonSingleSelect(
            selectedCategoryIndex,
            { index -> selectedCategoryIndex = index }
        )
        Spacer(Modifier.padding(BODY_CONTENT_PADDING.dp))
        SingleChoiceChipGroup(
            itemsMap =  combineIcons,
            selectedCategory = selectedCategoryIndex,
            selectedIndex = selectedChipIndex,
            onSelectedChange = {index ->
                selectedChipIndex = if(selectedChipIndex == index){
                        null
                    }else {
                        index
                    }
                },
            modifier = Modifier,
        )
    }
}


@Composable
fun SingleChoiceChipGroup(
    itemsMap:Map<Int, List<ChoiceChip>> ,
    selectedCategory : Int,
    selectedIndex: Int? ,
    onSelectedChange: (Int) -> Unit ,
    modifier: Modifier = Modifier ,
)
{
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsMap[selectedCategory].let {items ->
            items?.forEachIndexed { index, choiceChip, ->
                CustomElevatedFilterChip(
                    index = index,
                    selectedIndex =selectedIndex,
                    onSelectedChange = onSelectedChange,
                    label = choiceChip.label,
                    icon = choiceChip.icon

                )
            }
        }
    }
}

@Composable
fun CustomElevatedFilterChip(
    index:Int,
    selectedIndex: Int?,
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
fun SegmentedButtonSingleSelect( selectedIndex: Int, onClick:(index:Int)->Unit){

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
                onClick = { onClick(index)},
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
    val meleeItems :List<ChoiceChip> = listOf(
        ChoiceChip(Lucide.Axe,"Axes"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.club),"Clubs"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.sword), "Swords"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.spear), "Spears"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.polearm),"Poleartms"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.knife), "Knives"),
        ChoiceChip(Lucide.Grab,"Fists"),
        ChoiceChip(Lucide.Shield,"Shields")
    )
    val rangedItems :List<ChoiceChip> = listOf(
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.bow_arrow),"Bows"),
        ChoiceChip( ImageVector.vectorResource(id = R.drawable.crossbow),"Crossbows"),
    )
    val magicItems :List<ChoiceChip> = listOf(
        ChoiceChip(Lucide.WandSparkles,"Elemental magic"),
        ChoiceChip(Lucide.Wand,"Blood magic")
    )
    val ammoItems :List<ChoiceChip> = listOf(
        ChoiceChip( ImageVector.vectorResource(id = R.drawable.arrow),"Arrows"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.bolt),"Arrows"),
        ChoiceChip(ImageVector.vectorResource(id = R.drawable.missile),"Missiles"),
        ChoiceChip(Lucide.Bomb,"Bombs"),
    )
    val combineIcons : Map<Int, List<ChoiceChip>>  = mapOf(
        0 to meleeItems,
        1 to rangedItems,
        2 to magicItems,
        3 to  ammoItems
    )
    ValheimVikiAppTheme { SingleChoiceChipGroup(
        itemsMap = combineIcons,
        selectedCategory = 0,
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
    ValheimVikiAppTheme { SegmentedButtonSingleSelect(
        selectedIndex = 0,
        onClick = {  }
    ) }

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

