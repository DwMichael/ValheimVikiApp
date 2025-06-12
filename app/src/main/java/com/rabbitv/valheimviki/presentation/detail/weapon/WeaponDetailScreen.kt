package com.rabbitv.valheimviki.presentation.detail.weapon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.main_detail_image.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.topbar.SimpleTopBar
import com.rabbitv.valheimviki.presentation.detail.weapon.model.WeaponUiState
import com.rabbitv.valheimviki.presentation.detail.weapon.viewmodel.WeaponDetailViewModel
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite


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
    Scaffold{ innerPadding ->
        uiState.weapon?.let { weapon ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(sharedScrollState)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
            ) {
                MainDetailImage(
                    onBack = onBack,
                    imageUrl = weapon.imageUrl,
                    name = weapon.name,
                    textAlign = TextAlign.Center,
                    contentScale = ContentScale.Crop
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(BODY_CONTENT_PADDING.dp),
                    thickness = 1.dp,
                    color = PrimaryWhite
                )
                weapon.description?.let { text ->
                    DetailExpandableText(
                        text = text,
                        collapsedMaxLine = 3,
                        isExpanded = isExpandable
                    )
                }
            }

        }
    }

}


//@Preview(name = "WeaponDetailScreen")
//@Composable
//private fun PreviewWeaponDetailScreen() {
//    WeaponDetailScreen()
//}
