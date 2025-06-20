package com.rabbitv.valheimviki.presentation.detail.armor


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.card.LevelInfoCard
import com.rabbitv.valheimviki.presentation.components.card.card_image.CardImageWithTopLabel
import com.rabbitv.valheimviki.presentation.components.main_detail_image.AsyncImageAnimated
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.armor.viewmodel.ArmorDetailViewModel
import com.rabbitv.valheimviki.presentation.detail.weapon.model.ArmorUiState
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.fakeArmorList
import com.rabbitv.valheimviki.utils.mapUpgradeArmorInfoToGridList


@Composable
fun ArmorDetailScreen(
    onBack: () -> Unit,
    viewModel: ArmorDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArmorDetailContent(
        onBack  = onBack,
        uiState = uiState
    )
}

@Composable
fun ArmorDetailContent(
    onBack: () -> Unit,
    uiState: ArmorUiState
) {

    val isExpandable = remember { mutableStateOf(false) }


    Scaffold(
        containerColor = Color(0xFF0d1c1d)
    ) { innerPadding ->
        uiState.armor?.let { armor ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
            ) {
                AsyncImageAnimated(
                    onBack = onBack,
                    imageUrl = armor.imageUrl,
                    contentScale = ContentScale.FillBounds,
                    imageScale = 0.6f,
                    backgroundImageColor = Color(0xFF191e24),
                    height = 250.dp
                )
                Text(
                    armor.name,
                    modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp).padding(top =5.dp),
                    color = PrimaryWhite,
                    style = MaterialTheme.typography.displayMedium
                )
                SlavicDivider()
                armor.description?.let {
                    DetailExpandableText(
                        text = it,
                        collapsedMaxLine = 3,
                        isExpanded = isExpandable,
                    )
                }

                uiState.craftingObjects?.let { craftingStation ->
                    TridentsDividedRow()
                    CardImageWithTopLabel(
                        itemData = craftingStation,
                        subTitle = "Crafting Station Needed to Make This Item",
                        contentScale = ContentScale.FillBounds,
                    )
                }
                TridentsDividedRow()
                Text(
                    "Upgrade Information",
                    modifier = Modifier.padding(start = BODY_CONTENT_PADDING.dp, end =  BODY_CONTENT_PADDING.dp, bottom =  BODY_CONTENT_PADDING.dp),
                    color = PrimaryWhite,
                    style = MaterialTheme.typography.headlineMedium
                )

                armor.upgradeInfoList?.forEachIndexed { levelIndex, upgradeInfoForLevel ->
                    val upgradeStats = mapUpgradeArmorInfoToGridList(upgradeInfoForLevel)
                    Log.d("upgradeStats", upgradeStats.toString())
                    LevelInfoCard(
                        modifier = Modifier.padding(
                            horizontal = BODY_CONTENT_PADDING.dp,
                            vertical = 8.dp
                        ),
                        level = levelIndex,
                        upgradeStats = upgradeStats,
                        materialsForUpgrade = uiState.materials,
                    )
                }
                SlavicDivider()
                Text(
                    "Additional effect per level",
                    modifier = Modifier.padding(start = BODY_CONTENT_PADDING.dp, end =  BODY_CONTENT_PADDING.dp, bottom =  BODY_CONTENT_PADDING.dp),
                    color = PrimaryWhite,
                    style = MaterialTheme.typography.headlineMedium
                )
                armor.upgradeInfoList?.forEach { upgradeInfoForLevel ->
                    upgradeInfoForLevel.effect?.let {
                        Text(
                            it,
                            modifier = Modifier.padding(start = BODY_CONTENT_PADDING.dp, end =  BODY_CONTENT_PADDING.dp, bottom =  BODY_CONTENT_PADDING.dp),
                            color = PrimaryWhite,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Text(
                        "Usage",
                        modifier = Modifier.padding(start = BODY_CONTENT_PADDING.dp, end =  BODY_CONTENT_PADDING.dp, bottom =  BODY_CONTENT_PADDING.dp),
                        color = PrimaryWhite,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    upgradeInfoForLevel.usage?.let {
                        Text(
                            it,
                            modifier = Modifier.padding(start = BODY_CONTENT_PADDING.dp, end =  BODY_CONTENT_PADDING.dp, bottom =  BODY_CONTENT_PADDING.dp),
                            color = PrimaryWhite,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(45.dp))
            }
        }
    }
}


@Preview(name = "WeaponDetailScreen", showBackground = true)
@Composable
private fun PreviewWeaponDetailScreen() {
    ValheimVikiAppTheme {
        ArmorDetailContent(
            onBack = {},
            uiState = ArmorUiState(
                armor = fakeArmorList()[0],
                materials = emptyList(),
                craftingObjects = CraftingObject(
                    id = "1",
                    imageUrl = "",
                    category = "",
                    subCategory = "TODO()",
                    name = "Workbench",
                    description = "",
                    order = 1
                ),
                isLoading = false,
                error = null
            )
        )
    }

}
