package com.rabbitv.valheimviki.presentation.detail.weapon.model
import androidx.compose.ui.graphics.Color

enum class StatArmorVisuals(
    val displayName: String,
    val colorHex: Color
) {
    QUALITY("Quality", Color(0xFFfccd14)),
    ARMOR("Armor", Color(0xFF4169E1)),
    DURABILITY("Durability", Color(0xFF61a7fd)),
    STATION_LEVEL("Station level", Color(0xFF818cf8)),
    PRICE("Price", Color(0xFFFFD700)),
    EFFECTS("Effects", Color(0xFF9370DB)),
    USAGE("Usage", Color(0xFF808080));
}