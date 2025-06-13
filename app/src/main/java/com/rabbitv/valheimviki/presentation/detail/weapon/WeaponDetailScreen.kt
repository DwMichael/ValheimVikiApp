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
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponUiState
import com.rabbitv.valheimviki.presentation.detail.weapon.viewmodel.WeaponDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData.fakeWeaponList


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
    val isExpandableNote = remember { mutableStateOf(false) }
    Scaffold { innerPadding ->
        uiState.weapon?.let { weapon ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(sharedScrollState)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
            ) {
                MainDetailImage(
                    onBack = onBack,
                    imageUrl = weapon.imageUrl,
                    name = weapon.name,
                    textAlign = TextAlign.Center,
                    imageScale = 0.6f,
                    contentScale = ContentScale.FillBounds,
                    backgroundImageColor = Color(0xFF162021)
                )
                weapon.description?.let {
                    DetailExpandableText(
                        text = it,
                        collapsedMaxLine = 3,
                        isExpanded = isExpandable
                    )
                }
                Spacer(modifier = Modifier.padding(top = BODY_CONTENT_PADDING.dp))
                Text(
                    "Upgrade Information",
                    modifier = Modifier.padding(horizontal = BODY_CONTENT_PADDING.dp),
                    color = PrimaryWhite,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.padding(top = BODY_CONTENT_PADDING.dp))
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
