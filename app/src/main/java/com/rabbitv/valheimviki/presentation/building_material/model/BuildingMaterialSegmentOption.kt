package com.rabbitv.valheimviki.presentation.building_material.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Armchair
import com.composables.icons.lucide.Crosshair
import com.composables.icons.lucide.Hammer
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lamp
import com.composables.icons.lucide.Layers
import com.composables.icons.lucide.LayoutGrid
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
	override val labelRes: Int,
	override val value: BuildingMaterialSubCategory
) : GridCategoryOption<BuildingMaterialSubCategory> {
	WOOD(R.drawable.wood, Lucide.Trees, R.string.category_wood, BuildingMaterialSubCategory.WOOD),
	CORE_WOOD(
		R.drawable.core_wood,
		Lucide.Layers,
		R.string.category_core_wood,
		BuildingMaterialSubCategory.CORE_WOOD
	),
	RESOURCE(
		R.drawable.resources,
		Lucide.Package,
		R.string.category_resources,
		BuildingMaterialSubCategory.RESOURCE
	),
	STONE_AND_METAL(
		R.drawable.stone_and_metals,
		Lucide.Hammer,
		R.string.category_stone_and_metal,
		BuildingMaterialSubCategory.STONE_AND_METAL
	),
	LIGHT_SOURCE(
		R.drawable.light_source,
		Lucide.Lamp,
		R.string.category_lights,
		BuildingMaterialSubCategory.LIGHT_SOURCE
	),
	TRANSPORT(
		R.drawable.transport,
		Lucide.Truck,
		R.string.category_transports,
		BuildingMaterialSubCategory.TRANSPORT
	),
	FURNITURE(
		R.drawable.furniture,
		Lucide.Armchair,
		R.string.category_furniture,
		BuildingMaterialSubCategory.FURNITURE
	),
	DECORATIVE(
		R.drawable.decorative,
		Lucide.Star,
		R.string.category_decor,
		BuildingMaterialSubCategory.DECORATIVE
	),
	ROOF(R.drawable.roof, Lucide.House, R.string.category_roofs, BuildingMaterialSubCategory.ROOF),
	DEFENSE(R.drawable.defense, Lucide.Shield, R.string.category_defense, BuildingMaterialSubCategory.DEFENSE),
	SIEGE(R.drawable.siege, Lucide.Crosshair, R.string.category_siege, BuildingMaterialSubCategory.SIEGE),
	MISC(R.drawable.aprilfoolsimage, Lucide.LayoutGrid, R.string.category_misc, BuildingMaterialSubCategory.MISC)
}