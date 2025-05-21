package com.rabbitv.valheimviki.presentation.material.model

import com.rabbitv.valheimviki.domain.model.material.MaterialSubCategory
import com.rabbitv.valheimviki.presentation.components.grid.grid_category.GridCategoryOption


enum class MaterialSegmentOption(
    override val label: String,
    override val value: MaterialSubCategory
) : GridCategoryOption<MaterialSubCategory> {
    CREATURE_DROP("CREATURE DROPS", MaterialSubCategory.CREATURE_DROP),
    CRAFTED("CRAFTED", MaterialSubCategory.CRAFTED),
    MISCELLANEOUS("GENERAL", MaterialSubCategory.MISCELLANEOUS),
    MINI_BOSS_DROP("MINI BOSS DROPS", MaterialSubCategory.MINI_BOSS_DROP),
    GEMSTONE("GEMSTONES", MaterialSubCategory.GEMSTONE),
    BOSS_DROP("BOSS DROP", MaterialSubCategory.BOSS_DROP),
    SEED("SEEDS", MaterialSubCategory.SEED),
    FORSAKEN_ALTAR_OFFERING(
        "FORSAKEN ALTAR OFFERINGS",
        MaterialSubCategory.FORSAKEN_ALTAR_OFFERING
    ),
    METAL("METAL", MaterialSubCategory.METAL),
    SHOP("SHOP", MaterialSubCategory.SHOP),
    VALUABLE("VALUABLE", MaterialSubCategory.VALUABLE)
}