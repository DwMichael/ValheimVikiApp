package com.rabbitv.valheimviki.presentation.components.language_popup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.Globe
import com.composables.icons.lucide.Lucide
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.presentation.components.button.DarkGlassButton
import com.rabbitv.valheimviki.ui.theme.Shapes

@Composable
fun LanguageNotificationDialog(
    viewModel: LanguageNotificationViewModel = hiltViewModel()
) {
    val showPopup by viewModel.showPopup.collectAsStateWithLifecycle()
    val canDismiss by viewModel.canDismiss.collectAsStateWithLifecycle()
    val remainingSeconds by viewModel.remainingSeconds.collectAsStateWithLifecycle()

    if (showPopup) {
        Dialog(
            onDismissRequest = { if (canDismiss) viewModel.dismissPopup() },
            properties = DialogProperties(
                dismissOnBackPress = canDismiss,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            LanguageNotificationContent(
                canDismiss = canDismiss,
                remainingSeconds = remainingSeconds,
                onDismiss = { viewModel.dismissPopup() }
            )
        }
    }
}

@Composable
private fun LanguageNotificationContent(
    canDismiss: Boolean,
    remainingSeconds: Int,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentSize()
            .clip(Shapes.large)
            .border(
                width = 1.dp,
                color = Color(0xFF42586D).copy(alpha = 0.5f),
                shape = Shapes.large
            ),
        color = Color(0xFF0B1116)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Lucide.Globe,
                contentDescription = null,
                tint = Color(0xFFFF6B35),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.language_popup_title),
                color = Color(0xFFE8EDF3),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.language_popup_message),
                color = Color(0xFFAAB7C4),
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            DarkGlassButton(
                onCardClick = { if (canDismiss) onDismiss() },
                enabled = canDismiss,
                label = if (canDismiss) {
                    stringResource(R.string.language_popup_dismiss)
                } else {
                    stringResource(R.string.language_popup_wait, remainingSeconds)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
