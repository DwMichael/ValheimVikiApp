package com.rabbitv.valheimviki.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.composables.icons.lucide.Cog
import com.composables.icons.lucide.Lucide
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.rabbitv.valheimviki.domain.use_cases.app_update.UpdateState

@Composable
fun AppUpdateHandler(
	updateState: UpdateState,
	onStartImmediateUpdate: (AppUpdateInfo) -> Unit,
	onStartFlexibleUpdate: (AppUpdateInfo) -> Unit,
	onCompleteUpdate: () -> Unit,
	onDismissUpdate: () -> Unit
) {
	when (updateState) {
		is UpdateState.ImmediateUpdateRequired -> {
			ImmediateUpdateDialog(
				appUpdateInfo = updateState.appUpdateInfo,
				onUpdateClick = { onStartImmediateUpdate(updateState.appUpdateInfo) }
			)
		}

		is UpdateState.FlexibleUpdateAvailable -> {
			FlexibleUpdateDialog(
				appUpdateInfo = updateState.appUpdateInfo,
				onUpdateClick = { onStartFlexibleUpdate(updateState.appUpdateInfo) },
				onDismiss = onDismissUpdate
			)
		}

		is UpdateState.ReadyToInstall -> {
			UpdateReadySnackbar(
				onInstallClick = onCompleteUpdate,
				onDismiss = onDismissUpdate
			)
		}

		is UpdateState.Downloading -> {
			UpdateDownloadingIndicator()
		}

		is UpdateState.Failed -> {
			UpdateFailedDialog(
				message = updateState.message,
				onDismiss = onDismissUpdate
			)
		}

		else -> {
			// No UI needed for other states
		}
	}
}

@Composable
private fun ImmediateUpdateDialog(
	appUpdateInfo: AppUpdateInfo,
	onUpdateClick: () -> Unit
) {
	Dialog(
		onDismissRequest = { /* Cannot dismiss immediate update */ },
		properties = DialogProperties(
			dismissOnBackPress = false,
			dismissOnClickOutside = false
		)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
		) {
			Column(
				modifier = Modifier.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Icon(
					imageVector = Lucide.Cog,
					contentDescription = "Update Required",
					modifier = Modifier.size(48.dp),
					tint = MaterialTheme.colorScheme.primary
				)

				Spacer(modifier = Modifier.height(16.dp))

				Text(
					text = "Update Required",
					style = MaterialTheme.typography.headlineSmall,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onSurface
				)

				Spacer(modifier = Modifier.height(8.dp))

				Text(
					text = "A new version of Valheim Viki is available. This update is required to continue using the app.",
					style = MaterialTheme.typography.bodyMedium,
					textAlign = TextAlign.Center,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				Spacer(modifier = Modifier.height(24.dp))

				Button(
					onClick = onUpdateClick,
					modifier = Modifier.fillMaxWidth()
				) {
					Text("Update Now")
				}
			}
		}
	}
}

@Composable
private fun FlexibleUpdateDialog(
	appUpdateInfo: AppUpdateInfo,
	onUpdateClick: () -> Unit,
	onDismiss: () -> Unit
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		icon = {
			Icon(
				imageVector = Lucide.Cog,
				contentDescription = "Update Available",
				tint = MaterialTheme.colorScheme.primary
			)
		},
		title = {
			Text(
				text = "Update Available",
				style = MaterialTheme.typography.headlineSmall,
				fontWeight = FontWeight.Bold
			)
		},
		text = {
			Text(
				text = "A new version of Valheim Viki is available with improvements and bug fixes. Would you like to download it?",
				style = MaterialTheme.typography.bodyMedium
			)
		},
		confirmButton = {
			Button(onClick = onUpdateClick) {
				Text("Download")
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text("Later")
			}
		}
	)
}

@Composable
private fun UpdateReadySnackbar(
	onInstallClick: () -> Unit,
	onDismiss: () -> Unit
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.primaryContainer
		)
	) {
		Row(
			modifier = Modifier.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				imageVector = Lucide.Cog,
				contentDescription = "Update Ready",
				tint = MaterialTheme.colorScheme.onPrimaryContainer
			)

			Spacer(modifier = Modifier.width(12.dp))

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = "Update Ready",
					style = MaterialTheme.typography.titleSmall,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onPrimaryContainer
				)
				Text(
					text = "Restart the app to complete the update",
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onPrimaryContainer
				)
			}

			Spacer(modifier = Modifier.width(12.dp))

			TextButton(
				onClick = onInstallClick,
				colors = ButtonDefaults.textButtonColors(
					contentColor = MaterialTheme.colorScheme.primary
				)
			) {
				Text("RESTART")
			}
		}
	}
}

@Composable
private fun UpdateDownloadingIndicator() {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant
		)
	) {
		Row(
			modifier = Modifier.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			CircularProgressIndicator(
				modifier = Modifier.size(24.dp),
				color = MaterialTheme.colorScheme.primary,
				strokeWidth = 3.dp
			)

			Spacer(modifier = Modifier.width(12.dp))

			Text(
				text = "Downloading update...",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
private fun UpdateFailedDialog(
	message: String,
	onDismiss: () -> Unit
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		icon = {
			Icon(
				imageVector = Lucide.Cog,
				contentDescription = "Update Failed",
				tint = MaterialTheme.colorScheme.error
			)
		},
		title = {
			Text(
				text = "Update Failed",
				style = MaterialTheme.typography.headlineSmall,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.error
			)
		},
		text = {
			Text(
				text = message,
				style = MaterialTheme.typography.bodyMedium
			)
		},
		confirmButton = {
			Button(onClick = onDismiss) {
				Text("OK")
			}
		}
	)
}
