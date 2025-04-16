package com.rabbitv.valheimviki.presentation.detail.creature

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.navigation.LocalSharedTransitionScope
import com.rabbitv.valheimviki.presentation.components.DetailExpandableText
import com.rabbitv.valheimviki.presentation.components.MainDetailImage
import com.rabbitv.valheimviki.presentation.components.SlavicDivider

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CreatureDetailScreen(
    onBack: () -> Unit,
    viewModel: CreatureScreenViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val mainBoss by viewModel.mainBoss.collectAsStateWithLifecycle()
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    mainBoss?.let { mainBoss ->
        CreatureContent(
            onBack = onBack,
            animatedVisibilityScope = animatedVisibilityScope,
            mainBoss = mainBoss,
            sharedTransitionScope = sharedTransitionScope,
            errorPainter = TODO(),
        )
    }

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CreatureContent(
    mainBoss: MainBoss,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    errorPainter: Painter? = null,
){
    Scaffold(
        content = {
                padding ->
            Image(
                painter = painterResource(id = R.drawable.main_background),
                contentDescription = "BackgroundImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Column(
                modifier = Modifier
                    .testTag("BiomeDetailScreen")
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                MainDetailImage(
                    onBack = onBack,
                    itemData = mainBoss,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    errorPainter = errorPainter
                )
                DetailExpandableText(text = mainBoss.description.toString())
                SlavicDivider()
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
    name = "CreatureDetail",
    showBackground = true
    )
@Composable
private fun PreviewCreatureDetail() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true){
            CreatureDetailScreen(
                onBack = TODO(),
                viewModel = TODO(),
                animatedVisibilityScope = TODO()
            )
        }
    }

}