package com.rabbitv.valheimviki.domain.model.search

import androidx.room.DatabaseView
import com.rabbitv.valheimviki.domain.repository.ItemData

@DatabaseView(
	viewName = "search_view",
	value = """
        SELECT id, name, imageUrl, category, subCategory FROM biomes
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM creatures
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM food
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM armors
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM weapons
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM building_materials
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM materials
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM crafting_objects
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM tools
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM meads
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM point_of_interest
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM trees
        UNION ALL
        SELECT id, name, imageUrl, category, subCategory FROM ore_deposits
    """
)
data class SearchView(
	override val id: String,
	override val name: String,
	override val imageUrl: String,
	override val category: String,
	override val subCategory: String?
) : ItemData