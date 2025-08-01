package com.rabbitv.valheimviki.data.local.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rabbitv.valheimviki.data.local.converter.Converters
import com.rabbitv.valheimviki.data.local.dao.ArmorDao
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.BuildingMaterialDao
import com.rabbitv.valheimviki.data.local.dao.CraftingObjectDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.local.dao.FavoriteDao
import com.rabbitv.valheimviki.data.local.dao.FoodDao
import com.rabbitv.valheimviki.data.local.dao.MaterialDao
import com.rabbitv.valheimviki.data.local.dao.MeadDao
import com.rabbitv.valheimviki.data.local.dao.OreDepositDao
import com.rabbitv.valheimviki.data.local.dao.PointOfInterestDao
import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.local.dao.SearchDao
import com.rabbitv.valheimviki.data.local.dao.ToolDao
import com.rabbitv.valheimviki.data.local.dao.TreeDao
import com.rabbitv.valheimviki.data.local.dao.WeaponDao
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.model.search.SearchFTS
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

@Database(
	entities = [
		Favorite::class,
		Biome::class, Creature::class,
		Relation::class, OreDeposit::class,
		Material::class, PointOfInterest::class,
		Tree::class, Food::class, Weapon::class,
		Armor::class, Mead::class, ItemTool::class,
		BuildingMaterial::class, CraftingObject::class,
		Search::class,
		SearchFTS::class],
	version = 1,
	exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ValheimVikiDatabase : RoomDatabase() {

	companion object {
		fun create(context: Context, useInMemory: Boolean): ValheimVikiDatabase {
			val databaseBuilder = if (useInMemory) {
				Room.inMemoryDatabaseBuilder(context, ValheimVikiDatabase::class.java)
			} else {
				Room.databaseBuilder(context, ValheimVikiDatabase::class.java, "valheimviki.db")
			}
			return databaseBuilder.fallbackToDestructiveMigration(true).build()
		}

	}

	abstract fun searchDao(): SearchDao
	abstract fun favoriteDao(): FavoriteDao
	abstract fun biomeDao(): BiomeDao
	abstract fun creatureDao(): CreatureDao
	abstract fun relationDao(): RelationDao
	abstract fun oreDepositDao(): OreDepositDao
	abstract fun materialDao(): MaterialDao
	abstract fun pointOfInterestDao(): PointOfInterestDao
	abstract fun treeDao(): TreeDao
	abstract fun foodDao(): FoodDao
	abstract fun weaponDao(): WeaponDao
	abstract fun armorDao(): ArmorDao
	abstract fun meadDao(): MeadDao
	abstract fun toolDao(): ToolDao
	abstract fun buildingMaterialDao(): BuildingMaterialDao
	abstract fun craftingObjectDao(): CraftingObjectDao
}