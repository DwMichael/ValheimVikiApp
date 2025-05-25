package com.rabbitv.valheimviki.presentation.building_material.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Armchair
import com.composables.icons.lucide.Crosshair
import com.composables.icons.lucide.Hammer
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lamp
import com.composables.icons.lucide.Layers
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Package
import com.composables.icons.lucide.Shield
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Trees
import com.composables.icons.lucide.Truck
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterialSubCategory
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.GridCategoryOption


enum class BuildingMaterialSegmentOption(
    override val image: Int,
    override val icon: ImageVector,
    override val label: String,
    override val value: BuildingMaterialSubCategory
) : GridCategoryOption<BuildingMaterialSubCategory> {
    WOOD(R.drawable.wood, Lucide.Trees, "WOOD", BuildingMaterialSubCategory.WOOD),
    CORE_WOOD(
        R.drawable.core_wood,
        Lucide.Layers,
        "CORE WOOD",
        BuildingMaterialSubCategory.CORE_WOOD
    ),
    RESOURCE(
        R.drawable.resources,
        Lucide.Package,
        "RESOURCES",
        BuildingMaterialSubCategory.RESOURCE
    ),
    STONE_AND_METAL(
        R.drawable.stone_and_metals,
        Lucide.Hammer,
        "BLOCKS",
        BuildingMaterialSubCategory.STONE_AND_METAL
    ),
    TRANSPORT(
        R.drawable.transport,
        Lucide.Truck,
        "TRANSPORTS",
        BuildingMaterialSubCategory.TRANSPORT
    ),
    DEFENSE(R.drawable.defense, Lucide.Shield, "DEFENSE", BuildingMaterialSubCategory.DEFENSE),
    FURNITURE(
        R.drawable.furniture,
        Lucide.Armchair,
        "FURNITURE",
        BuildingMaterialSubCategory.FURNITURE
    ),
    SIEGE(R.drawable.siege, Lucide.Crosshair, "SIEGE", BuildingMaterialSubCategory.SIEGE),
    LIGHT_SOURCE(
        R.drawable.light_source,
        Lucide.Lamp,
        "LIGHTS",
        BuildingMaterialSubCategory.LIGHT_SOURCE
    ),
    DECORATIVE(
        R.drawable.decorative,
        Lucide.Star,
        "DECOR",
        BuildingMaterialSubCategory.DECORATIVE
    ),
    ROOF(R.drawable.roof, Lucide.House, "ROOFS", BuildingMaterialSubCategory.ROOF),
}