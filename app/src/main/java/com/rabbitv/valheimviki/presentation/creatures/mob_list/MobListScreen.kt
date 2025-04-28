package com.rabbitv.valheimviki.presentation.creatures.mob_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.presentation.components.ListContent

@Composable
fun MobListScreen(
    modifier: Modifier,
    paddingValues: PaddingValues,
    viewModel: MobListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
    ) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .testTag("AggressiveCreaturesSurface")
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if(uiState.aggressiveCreatures.isNotEmpty() == true)
            {
                ListContent(
                    items = uiState.aggressiveCreatures,
                    clickToNavigate = {}
                )
            }

        }
    }
}