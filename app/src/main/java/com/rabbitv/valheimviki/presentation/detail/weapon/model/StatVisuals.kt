package com.rabbitv.valheimviki.presentation.detail.weapon.model
import androidx.compose.ui.graphics.Color
enum class StatVisuals(
    val displayName: String,
    val colorHex: Color
) {
    QUALITY("Quality",  Color(0xFFfccd14)),
    FIRE_DAMAGE("Fire",  Color(0xFFFF4500)),
    FROST_DAMAGE("Frost", Color(0xFF87CEEB)),
    SLASH_DAMAGE("Slash",  Color(0xFFA9A9A9)),
    SPIRIT_DAMAGE("Spirit",  Color(0xFF9370DB)),
    DURABILITY("Durability",  Color(0xFF61a7fd)),
    STATION_LEVEL("Station level",  Color(0xFF818cf8)),
    LIGHTNING_DAMAGE("Lightning",  Color(0xFFFFFF00)),
    PIERCE_DAMAGE("Pierce",  Color(0xFF36454F)),
    BLUNT_DAMAGE("Blunt",  Color(0xFF654321)),
    POISON_DAMAGE("Poison",  Color(0xFF7CFC00)),
    CHOP_DAMAGE("Chop",  Color.Black),
    PURE_DAMAGE("Pure",  Color(0xFFF5F5F5)),
    PICKAXE_DAMAGE("Pickaxe",  Color(0xFF708090)),
    DAMAGE_ABSORBED_BLOOD_MAGIC_0("Base shield strength",  Color(0xFFDC143C)),
    MAXIMUM_SKELETONS("Max skeletons limit",  Color(0xFFF5F5DC)),
    DAMAGE_ABSORBED_BLOOD_MAGIC_100("Max shield strength", Color(0xFFDC143C)),
    CHOP_TREES("Chop trees",  Color(0xFF228B22));
}