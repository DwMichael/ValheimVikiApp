package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.data.repository.armor.ArmorRepositoryImpl
import com.rabbitv.valheimviki.data.repository.biome.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.building_material.BuildingMaterialRepositoryImpl
import com.rabbitv.valheimviki.data.repository.crafting_object.CraftingObjectRepositoryImpl
import com.rabbitv.valheimviki.data.repository.creature.CreatureRepositoryImpl
import com.rabbitv.valheimviki.data.repository.favorite.FavoriteRepositoryImpl
import com.rabbitv.valheimviki.data.repository.food.FoodRepositoryImpl
import com.rabbitv.valheimviki.data.repository.material.MaterialRepositoryImpl
import com.rabbitv.valheimviki.data.repository.mead.MeadRepositoryImpl
import com.rabbitv.valheimviki.data.repository.ore_deposit.OreDepositRepositoryImpl
import com.rabbitv.valheimviki.data.repository.point_of_interest.PointOfInterestRepositoryImpl
import com.rabbitv.valheimviki.data.repository.relation.RelationRepositoryImpl
import com.rabbitv.valheimviki.data.repository.search.SearchRepositoryImpl
import com.rabbitv.valheimviki.data.repository.tool.ToolRepositoryImpl
import com.rabbitv.valheimviki.data.repository.tree.TreeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.trinket.TrinketRepositoryImpl
import com.rabbitv.valheimviki.data.repository.weapon.WeaponRepositoryImplementation
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.repository.FavoriteRepository
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import com.rabbitv.valheimviki.domain.repository.MeadRepository
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import com.rabbitv.valheimviki.domain.repository.TrinketRepository
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


	@Binds
	@Singleton
	abstract fun bindSearchRepository(
		searchRepositoryImpl: SearchRepositoryImpl
	): SearchRepository

	@Binds
	@Singleton
	abstract fun bindFavoriteRepository(
		favoriteRepository: FavoriteRepositoryImpl
	): FavoriteRepository

	@Binds
	@Singleton
	abstract fun bindBiomeRepository(
		biomeRepositoryImpl: BiomeRepositoryImpl
	): BiomeRepository

	@Binds
	@Singleton
	abstract fun bindCreatureRepository(
		creatureRepositoryImpl: CreatureRepositoryImpl
	): CreatureRepository

	@Binds
	@Singleton
	abstract fun bindRelationRepository(
		relationRepositoryImpl: RelationRepositoryImpl
	): RelationRepository

	@Binds
	@Singleton
	abstract fun bindOreDepositRepository(
		oreDepositRepositoryImpl: OreDepositRepositoryImpl
	): OreDepositRepository

	@Binds
	@Singleton
	abstract fun bindMaterialsRepository(
		materialRepositoryImpl: MaterialRepositoryImpl
	): MaterialRepository

	@Binds
	@Singleton
	abstract fun bindPointOfInterestRepository(
		pointOfInterestRepositoryImpl: PointOfInterestRepositoryImpl
	): PointOfInterestRepository


	@Binds
	@Singleton
	abstract fun bindTreeRepository(
		treeRepositoryImpl: TreeRepositoryImpl
	): TreeRepository

	@Binds
	@Singleton
	abstract fun bindFoodRepository(
		foodRepositoryImpl: FoodRepositoryImpl
	): FoodRepository

	@Binds
	@Singleton
	abstract fun bindWeaponRepository(
		weaponRepositoryImplementation: WeaponRepositoryImplementation
	): WeaponRepository

	@Binds
	@Singleton
	abstract fun bindArmorRepository(
		armorRepository: ArmorRepositoryImpl
	): ArmorRepository


	@Binds
	@Singleton
	abstract fun bindMeadRepository(
		meadRepositoryImpl: MeadRepositoryImpl
	): MeadRepository

	@Binds
	@Singleton
	abstract fun bindToolRepository(
		toolRepositoryImpl: ToolRepositoryImpl
	): ToolRepository

	@Binds
	@Singleton
	abstract fun bindBuildingMaterialRepository(
		buildingMaterialRepositoryImpl: BuildingMaterialRepositoryImpl
	): BuildingMaterialRepository

	@Binds
	@Singleton
	abstract fun bindCraftingObjectRepository(
		craftingObjectRepositoryImpl: CraftingObjectRepositoryImpl
	): CraftingObjectRepository


	@Binds
	@Singleton
	abstract fun bindTrinketRepository(
		craftingObjectRepositoryImpl: TrinketRepositoryImpl
	): TrinketRepository


}