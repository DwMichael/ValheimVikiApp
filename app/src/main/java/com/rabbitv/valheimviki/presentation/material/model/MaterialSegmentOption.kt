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
import com.rabbitv.valheimviki.R
import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.GridCategoryOption


enum class MaterialSegmentOption(
    override val image: Int,
    override val icon: ImageVector,
    override val label: String,
    override val value: MaterialSubCategory
) : GridCategoryOption<MaterialSubCategory> {
    BOSS_DROP(R.drawable.boss, Lucide.Crown, "BOSS DROP", MaterialSubCategory.BOSS_DROP),
    MINI_BOSS_DROP(
        R.drawable.mini_boss_drop,
        Lucide.Skull,
        "MINI BOSS DROP",
        MaterialSubCategory.MINI_BOSS_DROP
    ),
    CREATURE_DROP(
        R.drawable.creature_drops,
        Lucide.Bone,
        "MOB DROP",
        MaterialSubCategory.CREATURE_DROP
    ),
    FORSAKEN_ALTAR_OFFERING(
        R.drawable.forsaken_altar, Lucide.Flame,
        "OFFERINGS",
        MaterialSubCategory.FORSAKEN_ALTAR_OFFERING
    ),
    CRAFTED(R.drawable.crafted, Lucide.Anvil, "CRAFTED", MaterialSubCategory.CRAFTED),
    METAL(R.drawable.metal, Lucide.Sprout, "METAL", MaterialSubCategory.METAL),
    MISCELLANEOUS(R.drawable.general, Lucide.Eclipse, "GENERAL", MaterialSubCategory.MISCELLANEOUS),
    GEMSTONE(R.drawable.gemstone, Lucide.Gem, "GEMSTONES", MaterialSubCategory.GEMSTONE),
    SEED(R.drawable.seed, Lucide.Cuboid, "SEEDS", MaterialSubCategory.SEED),
    SHOP(R.drawable.shop, Lucide.ShoppingCart, "SHOP", MaterialSubCategory.SHOP),
    VALUABLE(R.drawable.valuable, Lucide.Star, "VALUABLE", MaterialSubCategory.VALUABLE)
}