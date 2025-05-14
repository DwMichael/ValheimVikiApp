package com.rabbitv.valheimviki.presentation.weapons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.presentation.components.EmptyScreen
import com.rabbitv.valheimviki.presentation.components.ShimmerEffect
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme
import com.rabbitv.valheimviki.utils.FakeData


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
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponListStateRenderer(
    weaponListUiState: WeaponListUiState,
    paddingValues: PaddingValues
){
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("WeaponListSurface")
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when{
            weaponListUiState.isLoading ||  (weaponListUiState.weaponList.isEmpty() && weaponListUiState.isConnection) ->
            {
                ShimmerEffect()
            }

            (weaponListUiState.error != null || !weaponListUiState.isConnection) && weaponListUiState.weaponList.isEmpty() -> {
                Box(
                    modifier = Modifier.testTag("EmptyScreenWeaponList"),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("EmptyScreenWeaponList")
                    ) {
                        EmptyScreen(
                            modifier = Modifier.fillMaxSize(),
                            errorMessage = weaponListUiState.error
                                ?: stringResource(R.string.no_internet_connection_ms)
                        )
                    }
                }
            }

            else ->{

                WeaponListDisplay(
                    weaponListUiState =weaponListUiState
                )

            }

        }
    }
}


@Composable
fun WeaponListDisplay(
    weaponListUiState: WeaponListUiState
){
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text("Weapons",
            style = MaterialTheme.typography.displayLarge
        )
    }
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
        )
    }
}

