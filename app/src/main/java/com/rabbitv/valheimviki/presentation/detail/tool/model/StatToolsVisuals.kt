package com.rabbitv.valheimviki.presentation.detail.tool.model

import androidx.compose.ui.graphics.Color

enum class StatToolsVisuals(val colorHex: Color, val displayName: String) {
	PIERCE_DAMAGE(Color(0xFF36454F), "Pierce Damage"),
	PICKAXE_DAMAGE(Color(0xFF708090), "Pickaxe Damage"),
	QUALITY(Color(0xFFfccd14), "Quality"),
	DURABILITY(Color(0xFF61a7fd), "Durability"),
	STATION_LEVEL(Color(0xFF818cf8), "Station Level")
}