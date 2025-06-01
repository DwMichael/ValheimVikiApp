package com.rabbitv.valheimviki.presentation.components.floating_action_button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.ui.theme.BODY_CONTENT_PADDING
import com.rabbitv.valheimviki.ui.theme.ForestGreen10Dark
import com.rabbitv.valheimviki.ui.theme.ICON_SIZE
import com.rabbitv.valheimviki.ui.theme.PrimaryWhite
import com.rabbitv.valheimviki.ui.theme.ValheimVikiAppTheme

@Composable
fun CustomFloatingActionButton(
    showBackButton: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = showBackButton,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
        modifier = modifier.padding(25.dp)//TODO Sprawdzić czy dobrze to wygląda w liście itd
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = RoundedCornerShape(BODY_CONTENT_PADDING.dp),
            containerColor = ForestGreen10Dark,
            contentColor = PrimaryWhite,
            elevation = FloatingActionButtonDefaults.elevation(),
        ) {
            Icon(
                Icons.Filled.KeyboardArrowUp,
                contentDescription = "Button Up",
                modifier = Modifier.size(ICON_SIZE)
            )
        }
    }
}


@Preview("CustomFloatingActionButton", widthDp = 80, heightDp = 80)
@Composable
fun PreviewCustomFloatingActionButton() {
    val backButtonVisibleState = true

    val backToTopState = remember { mutableStateOf(false) }
    ValheimVikiAppTheme {
        CustomFloatingActionButton(
            showBackButton = backButtonVisibleState,
            onClick = { backToTopState.value = true },

        )
    }
}