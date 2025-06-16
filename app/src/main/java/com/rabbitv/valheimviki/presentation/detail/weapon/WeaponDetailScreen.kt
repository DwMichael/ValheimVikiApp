package com.rabbitv.valheimviki.presentation.detail.weapon


import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.SlavicDivider
import com.rabbitv.valheimviki.presentation.components.card.LevelInfoCard
import com.rabbitv.valheimviki.presentation.components.main_detail_image.AsyncImageAnimated
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponUiState
import com.rabbitv.valheimviki.presentation.detail.weapon.viewmodel.WeaponDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.ui.theme.YellowDT
import com.rabbitv.valheimviki.utils.FakeData.fakeWeaponList
import com.rabbitv.valheimviki.utils.mapUpgradeInfoToGridList


@Composable
fun WeaponDetailScreen(
    onBack: () -> Unit,
    viewModel: WeaponDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeaponDetailContent(
        onBack  = onBack,
        uiState = uiState
    )
}

@Composable
fun WeaponDetailContent(
    onBack: () -> Unit,
    uiState: WeaponUiState
) {

    val sharedScrollState = rememberScrollState()
    val isExpandable = remember { mutableStateOf(false) }


    Scaffold(
        containerColor = Color(0xFF0d1c1d)
    ) { innerPadding ->
        uiState.weapon?.let { weapon ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(sharedScrollState)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
            ) {
                AsyncImageAnimated(
                    onBack = onBack,
                    imageUrl = weapon.imageUrl,
                    contentScale = ContentScale.FillBounds,
                    imageScale = 0.5f,
                    backgroundImageColor = Color(0xFF191e24),
                    height = 250.dp
                )
                Text(
                    weapon.name,
                    modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp).padding(top =5.dp),
                    color = PrimaryWhite,
                    style = MaterialTheme.typography.displayMedium
                )
                SlavicDivider()
                weapon.description?.let {
                    DetailExpandableText(
                        text = it,
                        collapsedMaxLine = 3,
                        isExpanded = isExpandable,
                    )
                }
                TridentsDividedRow()
                Text(
                    "Upgrade Information",
                    modifier = Modifier.padding(start = BODY_CONTENT_PADDING.dp, end =  BODY_CONTENT_PADDING.dp, bottom =  BODY_CONTENT_PADDING.dp),
                    color = PrimaryWhite,
                    style = MaterialTheme.typography.headlineMedium
                )

                weapon.upgradeInfoList?.forEachIndexed { levelIndex, upgradeInfoForLevel ->
                    val gridInfoList = mapUpgradeInfoToGridList(upgradeInfoForLevel)
                    LevelInfoCard(
                        modifier = Modifier.padding(
                            horizontal = BODY_CONTENT_PADDING.dp,
                            vertical = 8.dp
                        ),
                        level = levelIndex,
                        upgradeInfoList = gridInfoList,
                        materialsForUpgrade = uiState.materials,
                        foodForUpgrade = uiState.foodAsMaterials
                    )
                }

                Spacer(modifier = Modifier.padding(45.dp))
            }
        }
    }
}


@Preview(name = "WeaponDetailScreen", showBackground = true)
@Composable
private fun PreviewWeaponDetailScreen() {
    ValheimVikiAppTheme {
        WeaponDetailContent(
            onBack = {},
            uiState = WeaponUiState(
                weapon = fakeWeaponList[0],
                materials = emptyList(),
                isLoading = false,
                error = null
            )
        )
    }

}
