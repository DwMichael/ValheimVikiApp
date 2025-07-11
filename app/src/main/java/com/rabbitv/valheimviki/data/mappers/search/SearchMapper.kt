package com.rabbitv.valheimviki.data.mappers.search

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.ItemData


fun ItemData.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}


fun Armor.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun Biome.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun BuildingMaterial.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun CraftingObject.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun Creature.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun Food.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun ItemTool.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun Material.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun Mead.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun OreDeposit.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun PointOfInterest.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun Tree.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}

fun Weapon.toSearch(): Search {
	return Search(
		id = this.id,
		name = this.name,
		description = this.description,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
	)
}


fun List<ItemData>.toSearchList(): List<Search> {
	return this.map { it.toSearch() }
}

//fun List<Armor>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<Biome>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<BuildingMaterial>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<CraftingObject>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<Creature>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<Food>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<ItemTool>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<Material>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<Mead>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<OreDeposit>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<PointOfInterest>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<Tree>.toSearchList(): List<Search> = map { it.toSearch() }
//fun List<Weapon>.toSearchList(): List<Search> = map { it.toSearch() }