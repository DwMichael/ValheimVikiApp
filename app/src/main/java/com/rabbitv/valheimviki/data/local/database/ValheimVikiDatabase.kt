package com.rabbitv.valheimviki.data.local.database


import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
import com.rabbitv.valheimviki.data.local.dao.TrinketDao
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
import com.rabbitv.valheimviki.domain.model.trinket.Trinket
import com.rabbitv.valheimviki.domain.model.weapon.Weapon

@Database(
	entities = [
		Favorite::class,
		Biome::class, Creature::class,
		Relation::class, OreDeposit::class,
		Material::class, PointOfInterest::class,
		Tree::class, Food::class, Weapon::class,
		Armor::class, Trinket::class, Mead::class, ItemTool::class,
		BuildingMaterial::class, CraftingObject::class,
		Search::class,
		SearchFTS::class],
	version = 3,
	exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ValheimVikiDatabase : RoomDatabase() {

	companion object {
		val MIGRATION_1_2 = object : Migration(1, 2) {
			override fun migrate(db: SupportSQLiteDatabase) {

			}
		}
		val MIGRATION_2_3 = object : Migration(2, 3) {
			override fun migrate(db: SupportSQLiteDatabase) {
				Log.w(
					"DB_MIGRATION",
					"RUNNING MIGRATION 2 -> 3. Deleting all data."
				) // <-- ADD THIS
				db.execSQL("DELETE FROM biomes")
				db.execSQL("DELETE FROM creatures")
				db.execSQL("DELETE FROM relations")
				db.execSQL("DELETE FROM ore_deposits")
				db.execSQL("DELETE FROM materials")
				db.execSQL("DELETE FROM point_of_interest")
				db.execSQL("DELETE FROM trees")
				db.execSQL("DELETE FROM food")
				db.execSQL("DELETE FROM weapons")
				db.execSQL("DELETE FROM armors")
				db.execSQL("DELETE FROM trinkets")
				db.execSQL("DELETE FROM meads")
				db.execSQL("DELETE FROM building_materials")
				db.execSQL("DELETE FROM crafting_objects")
				db.execSQL("DELETE FROM search")
				db.execSQL("DELETE FROM search_fts")
			}
		}

		fun create(context: Context, useInMemory: Boolean): ValheimVikiDatabase {
			val databaseBuilder = if (useInMemory) {
				Room.inMemoryDatabaseBuilder(context, ValheimVikiDatabase::class.java)
			} else {
				Room.databaseBuilder(context, ValheimVikiDatabase::class.java, "valheimviki.db")
			}
			return databaseBuilder.build()
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

	abstract fun trinketDao(): TrinketDao
}