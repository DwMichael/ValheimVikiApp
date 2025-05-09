package com.rabbitv.valheimviki.presentation.detail.creature.npc

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.ui.theme.DarkWood
import com.rabbitv.valheimviki.ui.theme.LightDark
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import kotlinx.coroutines.delay


@Composable
fun NpcDetailScreen(
    onBack: () -> Unit,
    viewModel: NpcDetailScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NpcDetailContent(
        uiState = uiState,
        onBack = onBack
    )

}


@Composable
fun NpcDetailContent(
    onBack: () -> Unit,
    uiState: NpcDetailUiState,
) {
    val backButtonVisibleState = remember { mutableStateOf(false) }
    mutableListOf<String?>(uiState.npc?.imageUrl)
    LaunchedEffect(Unit) {
        delay(450)
        backButtonVisibleState.value = true
    }

    Scaffold { padding ->
        uiState.npc?.let {
        }
    }
}







@Preview(name = "CreaturePage")
@Composable
fun PreviewCreaturePage() {
    AggressiveCreature(
        id = "1",
        category = "asd",
        subCategory = "sdasd",
        imageUrl = "sadasd",
        name = "sadsdd",
        description = "asdasd2",
        order = 2,
        weakness = "SDASD",
        resistance = "dasdas2",
        baseDamage = "dsasdasd",
        levels = listOf(
            LevelCreatureData(
                level = 1,
                baseHp = 23123,
                baseDamage = "DAsdasd",
                image = "dsadasd"
            )
        ),
        abilities = "SDASDAD"
    )
    remember { mutableStateOf(true) }

}


