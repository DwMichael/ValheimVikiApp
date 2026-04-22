package com.rabbitv.valheimviki.presentation.material.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Anvil
import com.composables.icons.lucide.Bone
import com.composables.icons.lucide.Crown
import com.composables.icons.lucide.Cuboid
import com.composables.icons.lucide.Eclipse
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Gem
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ShoppingCart
import com.composables.icons.lucide.Skull
import com.composables.icons.lucide.Sprout
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Trees
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.GridCategoryOption


enum class MaterialSegmentOption(
	override val image: Int,
	override val icon: ImageVector,
	override val labelRes: Int,
	override val value: MaterialSubCategory
) : GridCategoryOption<MaterialSubCategory> {
	BOSS_DROP(R.drawable.boss, Lucide.Crown, R.string.category_boss_drop, MaterialSubCategory.BOSS_DROP),
	MINI_BOSS_DROP(
		R.drawable.mini_boss_drop,
		Lucide.Skull,
		R.string.category_mini_boss_drop,
		MaterialSubCategory.MINI_BOSS_DROP
	),
	CREATURE_DROP(
		R.drawable.creature_drops,
		Lucide.Bone,
		R.string.category_mob_drop,
		MaterialSubCategory.CREATURE_DROP
	),
	FORSAKEN_ALTAR_OFFERING(
		R.drawable.forsaken_altar, Lucide.Flame,
		R.string.category_offerings,
		MaterialSubCategory.FORSAKEN_ALTAR_OFFERING
	),
	CRAFTED(R.drawable.crafted, Lucide.Anvil, R.string.category_crafted, MaterialSubCategory.CRAFTED),
	METAL(R.drawable.metal, Lucide.Sprout, R.string.category_metal, MaterialSubCategory.METAL),
	MISCELLANEOUS(R.drawable.general, Lucide.Eclipse, R.string.category_general, MaterialSubCategory.MISCELLANEOUS),
	GEMSTONE(R.drawable.gemstone, Lucide.Gem, R.string.category_gemstones, MaterialSubCategory.GEMSTONE),
	SEED(R.drawable.seed, Lucide.Cuboid, R.string.category_seeds, MaterialSubCategory.SEED),
	SHOP(R.drawable.shop, Lucide.ShoppingCart, R.string.category_shop, MaterialSubCategory.SHOP),
	VALUABLE(R.drawable.valuable, Lucide.Star, R.string.category_valuable, MaterialSubCategory.VALUABLE),
	WOOD(R.drawable.wood, Lucide.Trees, R.string.category_wood, MaterialSubCategory.WOOD)
}