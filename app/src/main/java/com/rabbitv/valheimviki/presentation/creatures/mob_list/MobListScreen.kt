package com.rabbitv.valheimviki.presentation.creatures.mob_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Rat
import com.composables.icons.lucide.Skull
import com.composables.icons.lucide.User
import com.rabbitv.valheimviki.domain.model.creature.CreatureSubCategory
import com.rabbitv.valheimviki.presentation.components.ListContent
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ICON_CLICK_DIM
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE
import com.rabbitv.valheimviki.ui.theme.PrimaryText
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ShimmerDarkGray
import kotlinx.coroutines.launch

@Composable
fun MobListScreen(
    onItemClick: (String, Int) -> Unit,
    modifier: Modifier, paddingValues: PaddingValues, viewModel: MobListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val initialScrollPosition by viewModel.scrollPosition.collectAsStateWithLifecycle()
    val selectedDifferentCategory = remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialScrollPosition
    )
    val coroutineScope = rememberCoroutineScope()
    val backButtonVisibleState by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 2 }
    }

    val backToTopState = remember { mutableStateOf(false) }

    if (backToTopState.value) {
        LaunchedEffect(backToTopState.value) {
            lazyListState.animateScrollToItem(0)
            viewModel.saveScrollPosition(0)
            backToTopState.value = false
        }
    }

    LaunchedEffect(lazyListState) {
        coroutineScope.launch {
            if (lazyListState.firstVisibleItemIndex >= 0) {
                viewModel.saveScrollPosition(lazyListState.firstVisibleItemIndex)
            }
        }
    }
    LaunchedEffect(uiState.selectedSubCategory) {
        if (selectedDifferentCategory.value) {
            coroutineScope.launch {
                lazyListState.scrollToItem(0)
                viewModel.saveScrollPosition(0)
            }
            selectedDifferentCategory.value = false
        }
    }

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .testTag("AggressiveCreaturesSurface")
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column {
            CreatureTab(
                selectedTabIndex = uiState.selectedSubCategory,
                onTabSelected = { index ->
                    when (index) {
                        0 -> viewModel.selectCreaturesSubCategory(CreatureSubCategory.PASSIVE_CREATURE)
                        1 -> viewModel.selectCreaturesSubCategory(CreatureSubCategory.AGGRESSIVE_CREATURE)
                        2 -> viewModel.selectCreaturesSubCategory(CreatureSubCategory.NPC)
                    }
                    selectedDifferentCategory.value = true
                }, onClick = {}, selected = true
            )
            if (uiState.creatureList.isNotEmpty() == true) {
                ListContent(
                    items = uiState.creatureList,
                    clickToNavigate = onItemClick,
                    lazyListState = lazyListState,
                    uiState.selectedSubCategory,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = backButtonVisibleState,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    onClick = {
                        backToTopState.value = true
                    },
                    shape = RoundedCornerShape(BODY_CONTENT_PADDING.dp),
                    containerColor = ForestGreen10Dark,
                    contentColor = PrimaryWhite,
                    elevation = FloatingActionButtonDefaults.elevation(),
                    modifier = Modifier.size(ICON_CLICK_DIM)
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Button Up",
                        modifier = Modifier.size(ICON_SIZE)
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatureTab(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onClick: () -> Unit,
    selected: Boolean,
) {
    val titles = listOf("PASSIVE", "AGGRESSIVE", "NPC")
    val icons = listOf(Lucide.Rat, Lucide.Skull, Lucide.User)

    val selectedColor = PrimaryText
    val unselectedColor = PrimaryWhite.copy(alpha = 0.6f)
    Tab(
        selected = selected,
        onClick = onClick,
    ) {
        Column(
            Modifier
                .padding(bottom = 16.dp)
                .height(80.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            PrimaryTabRow(
                modifier = Modifier.height(65.dp),
                selectedTabIndex = selectedTabIndex,
                containerColor = ShimmerDarkGray,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTabIndex,
                            matchContentSize = true
                        ),
                        width = 64.dp,
                        height = 3.dp,
                        color = selectedColor
                    )


                }
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            onTabSelected(index)
                        },

                        content = {
                            Column(
                                modifier = Modifier.padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = "Aggressive Creatures",
                                    tint = if (selectedTabIndex == index) selectedColor else unselectedColor
                                )
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = if (selectedTabIndex == index) selectedColor else unselectedColor

                                )
                            }
                        },
                    )
                }
            }
        }
    }
}

