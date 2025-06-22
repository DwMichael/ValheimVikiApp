package com.rabbitv.valheimviki.di

import android.content.Context
import com.rabbitv.valheimviki.data.local.dao.ArmorDao
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.BuildingMaterialDao
import com.rabbitv.valheimviki.data.local.dao.CraftingObjectDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.local.dao.FoodDao
import com.rabbitv.valheimviki.data.local.dao.MaterialDao
import com.rabbitv.valheimviki.data.local.dao.MeadDao
import com.rabbitv.valheimviki.data.local.dao.OreDepositDao
import com.rabbitv.valheimviki.data.local.dao.PointOfInterestDao
import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.local.dao.ToolDao
import com.rabbitv.valheimviki.data.local.dao.TreeDao
import com.rabbitv.valheimviki.data.local.dao.WeaponDao
import com.rabbitv.valheimviki.data.remote.api.ApiArmorService
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiBuildingMaterialService
import com.rabbitv.valheimviki.data.remote.api.ApiCraftingService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.remote.api.ApiFoodService
import com.rabbitv.valheimviki.data.remote.api.ApiMaterialsService
import com.rabbitv.valheimviki.data.remote.api.ApiMeadService
import com.rabbitv.valheimviki.data.remote.api.ApiOreDepositService
import com.rabbitv.valheimviki.data.remote.api.ApiPointOfInterestService
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.data.remote.api.ApiToolService
import com.rabbitv.valheimviki.data.remote.api.ApiTreeService
import com.rabbitv.valheimviki.data.remote.api.ApiWeaponService
import com.rabbitv.valheimviki.data.repository.DataStoreOperationsImpl
import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import com.rabbitv.valheimviki.data.repository.armor.ArmorRepositoryImpl
import com.rabbitv.valheimviki.data.repository.biome.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.building_material.BuildingMaterialRepositoryImpl
import com.rabbitv.valheimviki.data.repository.crafting_object.CraftingObjectRepositoryImpl
import com.rabbitv.valheimviki.data.repository.creature.CreatureRepositoryImpl
import com.rabbitv.valheimviki.data.repository.food.FoodRepositoryImpl
import com.rabbitv.valheimviki.data.repository.material.MaterialRepositoryImpl
import com.rabbitv.valheimviki.data.repository.mead.MeadRepositoryImpl
import com.rabbitv.valheimviki.data.repository.ore_deposit.OreDepositRepositoryImpl
import com.rabbitv.valheimviki.data.repository.point_of_interest.PointOfInterestRepositoryImpl
import com.rabbitv.valheimviki.data.repository.relation.RelationRepositoryImpl
import com.rabbitv.valheimviki.data.repository.tool.ToolRepositoryImpl
import com.rabbitv.valheimviki.data.repository.tree.TreeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.weapon.WeaponRepositoryImplementation
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import com.rabbitv.valheimviki.domain.repository.MeadRepository
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import com.rabbitv.valheimviki.domain.use_cases.armor.ArmorUseCases
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armor_by_id.GetArmorByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_armors_by_sub_category_use_case.GetArmorsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.armor.get_local_armors_use_case.GetLocalArmorsUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_local_biomes.GetLocalBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.BuildMaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_material_by_id.GetBuildMaterialByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_ids.GetBuildMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory.GetBuildMaterialsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_building_materials_by_subcategory_and_subtype.GetBuildMaterialsBySubCategoryAndSubTypeUseCase
import com.rabbitv.valheimviki.domain.use_cases.building_material.get_local_building_materials.GetLocalBuildMaterialsUseCase
import com.rabbitv.valheimviki.domain.use_cases.connection.NetworkConnectivityObserver
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.CraftingObjectUseCases
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_ids.GetCraftingObjectByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_sub_category_use_case.GetCraftingObjectsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_objects_by_ids.GetCraftingObjectsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_local_crafting_object_use_case.GetLocalCraftingObjectsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.get_aggressive_creatures.GetAggressiveCreatures
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id.GetCreatureByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id_and_subcategory.GetCreatureByIdAndSubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_relation_and_sub_category.GetCreatureByRelationAndSubCategory
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_subcategory.GetCreatureBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_ids.GetCreaturesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_local_creatures.GetLocalCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_main_bosses.GetMainBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_mini_bosses.GetMiniBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_npcs.GetNPCsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_passive_creatures.GetPassiveCreature
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_by_id.GetFoodByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_ids.GetFoodListByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_subCategory.GetFoodListBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_local_food_list.GetLocalFoodListUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.get_local_Materials.GetLocalMaterialsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_material_by_id.GetMaterialByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids.GetMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory.GetMaterialsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory_and_subtype.GetMaterialsBySubCategoryAndSubTypeUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.get_local_meads_use_case.GetLocalMeadsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_sub_category_use_case.GetMeadsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit.GetLocalOreDepositUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id.GetOreDepositByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids.GetOreDepositsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_local_point_of_interest.GetLocalPointOfInterestUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_id.GetPointOfInterestByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory.GetPointsOfInterestBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory_and_id.GetPointOfInterestBySubCategoryAndIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interests_by_ids.GetPointsOfInterestByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation.GetRelatedIdsRelationUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_related_by_id.GetItemRelatedById
import com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations.GetLocalRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_related_ids_for.GetRelatedIdsForUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.ToolUseCases
import com.rabbitv.valheimviki.domain.use_cases.tool.get_local_tools_use_case.GetLocalToolsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tool.get_tools_by_sub_category_use_case.GetToolsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.get_local_trees.GetLocalTreesUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_tree_by_id.GetTreeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_trees_by_ids.GetTreesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.WeaponUseCases
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_local_weapons_use_case.GetLocalWeaponsUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapon_by_id_use_case.GetWeaponByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_sub_category_use_case.GetWeaponsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_sub_type_use_case.GetWeaponsBySubTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

	@Provides
	@Singleton
	fun provideDataStoreOperationsImpl(
		@ApplicationContext context: Context
	): DataStoreOperations {
		return DataStoreOperationsImpl(context = context)
	}

	@Provides
	@Singleton
	fun provideBiomeRepositoryImpl(
		apiService: ApiBiomeService,
		biomeDao: BiomeDao
	): BiomeRepository {
		return BiomeRepositoryImpl(apiService, biomeDao)
	}

	@Provides
	@Singleton
	fun provideCreatureRepositoryImpl(
		apiService: ApiCreatureService,
		creatureDao: CreatureDao
	): CreatureRepository {
		return CreatureRepositoryImpl(apiService, creatureDao)
	}

	@Provides
	@Singleton
	fun provideRelationRepositoryImpl(
		apiService: ApiRelationsService,
		relationDao: RelationDao
	): RelationRepository {
		return RelationRepositoryImpl(apiService, relationDao)
	}

	@Provides
	@Singleton
	fun provideOreDepositRepositoryImpl(
		apiService: ApiOreDepositService,
		oreDepositDao: OreDepositDao
	): OreDepositRepository {
		return OreDepositRepositoryImpl(apiService, oreDepositDao)
	}

	@Provides
	@Singleton
	fun provideMaterialsRepositoryImpl(
		apiService: ApiMaterialsService,
		materialDao: MaterialDao
	): MaterialRepository {
		return MaterialRepositoryImpl(apiService, materialDao)
	}

	@Provides
	@Singleton
	fun providePointOfInterestRepositoryImpl(
		apiService: ApiPointOfInterestService,
		pointOfInterestDao: PointOfInterestDao
	): PointOfInterestRepository {
		return PointOfInterestRepositoryImpl(apiService, pointOfInterestDao)
	}


	@Provides
	@Singleton
	fun provideTreeRepositoryImpl(
		apiService: ApiTreeService,
		treeDao: TreeDao
	): TreeRepository {
		return TreeRepositoryImpl(apiService, treeDao)
	}

	@Provides
	@Singleton
	fun provideFoodRepositoryImpl(
		apiService: ApiFoodService,
		treeDao: FoodDao
	): FoodRepository {
		return FoodRepositoryImpl(apiService, treeDao)
	}

	@Provides
	@Singleton
	fun provideWeaponRepositoryImpl(
		apiService: ApiWeaponService,
		weaponDao: WeaponDao
	): WeaponRepository {
		return WeaponRepositoryImplementation(apiService, weaponDao)
	}

	@Provides
	@Singleton
	fun provideArmorRepositoryImpl(
		apiService: ApiArmorService,
		armorDao: ArmorDao
	): ArmorRepository {
		return ArmorRepositoryImpl(apiService, armorDao)
	}

	@Provides
	@Singleton
	fun provideMeadRepositoryImpl(
		apiService: ApiMeadService,
		meadDao: MeadDao
	): MeadRepository {
		return MeadRepositoryImpl(apiService, meadDao)
	}

	@Provides
	@Singleton
	fun provideToolRepositoryImpl(
		apiService: ApiToolService,
		toolDao: ToolDao
	): ToolRepository {
		return ToolRepositoryImpl(apiService, toolDao)
	}

	@Provides
	@Singleton
	fun provideBuildingMaterialRepositoryImpl(
		apiService: ApiBuildingMaterialService,
		buildingMaterialDao: BuildingMaterialDao
	): BuildingMaterialRepository {
		return BuildingMaterialRepositoryImpl(apiService, buildingMaterialDao)
	}

	@Provides
	@Singleton
	fun provideCraftingObjectRepositoryImpl(
		apiService: ApiCraftingService,
		craftingObjectDao: CraftingObjectDao
	): CraftingObjectRepository {
		return CraftingObjectRepositoryImpl(apiService, craftingObjectDao)
	}

	@Provides
	@Singleton
	fun provideNetworkConnectivityObserver(
		@ApplicationContext context: Context
	): NetworkConnectivity {
		return NetworkConnectivityObserver(context = context)
	}

	@Provides
	@Singleton
	fun provideRefetchUseCase(
		biomeRepository: BiomeRepository,
		creatureRepository: CreatureRepository,
		relationsRepository: RelationRepository,
		materialRepository: MaterialRepository,
		oreDepositRepository: OreDepositRepository,
		pointOfInterestRepository: PointOfInterestRepository,
		treeRepository: TreeRepository,
		foodRepository: FoodRepository,
		weaponRepository: WeaponRepository,
		armorRepository: ArmorRepository,
		meadRepository: MeadRepository,
		toolRepository: ToolRepository,
		buildingMaterialRepository: BuildingMaterialRepository,
		craftingObjectRepository: CraftingObjectRepository,
		dataStoreUseCases: DataStoreUseCases
	): DataRefetchUseCase {
		return DataRefetchUseCase(
			creatureRepository = creatureRepository,
			relationsRepository = relationsRepository,
			biomeRepository = biomeRepository,
			oreDepositRepository = oreDepositRepository,
			dataStoreUseCases = dataStoreUseCases,
			materialsRepository = materialRepository,
			pointOfInterestRepository = pointOfInterestRepository,
			treeRepository = treeRepository,
			foodRepository = foodRepository,
			weaponRepository = weaponRepository,
			armorRepository = armorRepository,
			meadRepository = meadRepository,
			toolRepository = toolRepository,
			buildingMaterialRepository = buildingMaterialRepository,
			craftingObjectRepository = craftingObjectRepository
		)
	}

	@Provides
	@Singleton
	fun provideBiomeUseCases(biomeRepository: BiomeRepository): BiomeUseCases {
		return BiomeUseCases(
			getLocalBiomesUseCase = GetLocalBiomesUseCase(biomeRepository),
			getBiomeByIdUseCase = GetBiomeByIdUseCase(biomeRepository),
		)
	}

	@Provides
	@Singleton
	fun provideCreatureUseCases(
		creatureRepository: CreatureRepository,
	): CreatureUseCases {
		return CreatureUseCases(
			getCreaturesByIds = GetCreaturesByIdsUseCase(creatureRepository),
			getCreatureById = GetCreatureByIdUseCase(creatureRepository),
			getCreatureByIdAndSubCategoryUseCase = GetCreatureByIdAndSubCategoryUseCase(
				creatureRepository
			),
			getMainBossesUseCase = GetMainBossesUseCase(creatureRepository),
			getMiniBossesUseCase = GetMiniBossesUseCase(creatureRepository),
			getAggressiveCreatures = GetAggressiveCreatures(creatureRepository),
			getPassiveCreature = GetPassiveCreature(creatureRepository),
			getNPCsUseCase = GetNPCsUseCase(creatureRepository),
			getLocalCreaturesUseCase = GetLocalCreaturesUseCase(creatureRepository),
			getCreatureByRelationAndSubCategory = GetCreatureByRelationAndSubCategory(
				creatureRepository
			),
			getCreaturesBySubCategory = GetCreatureBySubCategoryUseCase(creatureRepository),
		)
	}

	@Provides
	@Singleton
	fun provideRelationUseCases(relationsRepository: RelationRepository): RelationUseCases {
		return RelationUseCases(
			getRelatedIdUseCase = GetItemRelatedById(relationsRepository),
			getRelatedIdsUseCase = GetRelatedIdsRelationUseCase(relationsRepository),
			getLocalRelationsUseCase = GetLocalRelationsUseCase(relationsRepository),
			getRelatedIdsForUseCase = GetRelatedIdsForUseCase(relationsRepository)
		)
	}

	@Provides
	@Singleton
	fun provideDataStoreUseCases(dataStoreRepository: DataStoreRepository): DataStoreUseCases {
		return DataStoreUseCases(
			saveOnBoardingState = SaveOnBoardingState(dataStoreRepository),
			readOnBoardingUseCase = ReadOnBoardingState(dataStoreRepository),
			languageProvider = LanguageProvider(dataStoreRepository),
			saveLanguageState = SaveLanguageState(dataStoreRepository),
		)
	}

	@Provides
	@Singleton
	fun provideOreDepositUseCases(oreDepositRepository: OreDepositRepository): OreDepositUseCases {
		return OreDepositUseCases(
			getLocalOreDepositsUseCase = GetLocalOreDepositUseCase(oreDepositRepository),
			getOreDepositsByIdsUseCase = GetOreDepositsByIdsUseCase(oreDepositRepository),
			getOreDepositByIdUseCase = GetOreDepositByIdUseCase(oreDepositRepository),
		)
	}

	@Provides
	@Singleton
	fun provideMaterialUseCases(materialRepository: MaterialRepository): MaterialUseCases {
		return MaterialUseCases(
			getLocalMaterials = GetLocalMaterialsUseCase(materialRepository),
			getMaterialsByIds = GetMaterialsByIdsUseCase(materialRepository),
			getMaterialById = GetMaterialByIdUseCase(materialRepository),
			getMaterialsBySubCategory = GetMaterialsBySubCategoryUseCase(materialRepository),
			getMaterialsBySubCategoryAndSubType = GetMaterialsBySubCategoryAndSubTypeUseCase(
				materialRepository
			)
		)
	}

	@Provides
	@Singleton
	fun providePointOfInterestUseCases(pointOfInterestRepository: PointOfInterestRepository): PointOfInterestUseCases {
		return PointOfInterestUseCases(
			getLocalPointOfInterestUseCase = GetLocalPointOfInterestUseCase(
				pointOfInterestRepository
			),
			getPointOfInterestByIdUseCase = GetPointOfInterestByIdUseCase(pointOfInterestRepository),
			getPointsOfInterestBySubCategoryUseCase = GetPointsOfInterestBySubCategoryUseCase(
				pointOfInterestRepository
			),
			getPointOfInterestBySubCategoryAndIdUseCase = GetPointOfInterestBySubCategoryAndIdUseCase(
				pointOfInterestRepository
			),
			getPointsOfInterestByIdsUseCase = GetPointsOfInterestByIdsUseCase(
				pointOfInterestRepository
			)
		)
	}

	@Provides
	@Singleton
	fun provideTreesUseCases(treeRepository: TreeRepository): TreeUseCases {
		return TreeUseCases(
			getLocalTreesUseCase = GetLocalTreesUseCase(treeRepository),
			getTreeByIdUseCase = GetTreeByIdUseCase(treeRepository),
			getTreesByIdsUseCase = GetTreesByIdsUseCase(treeRepository)
		)
	}

	@Provides
	@Singleton
	fun provideFoodUseCases(foodRepository: FoodRepository): FoodUseCases {
		return FoodUseCases(
			getLocalFoodListUseCase = GetLocalFoodListUseCase(foodRepository),
			getFoodBySubCategoryUseCase = GetFoodListBySubCategoryUseCase(foodRepository),
			getFoodListByIdsUseCase = GetFoodListByIdsUseCase(foodRepository),
			getFoodByIdUseCase = GetFoodByIdUseCase(foodRepository)
		)
	}

	@Provides
	@Singleton
	fun provideWeaponUseCases(weaponRepository: WeaponRepository): WeaponUseCases {
		return WeaponUseCases(
			getLocalWeaponsUseCase = GetLocalWeaponsUseCase(weaponRepository),
			getWeaponsBySubCategoryUseCase = GetWeaponsBySubCategoryUseCase(),
			getWeaponsBySubTypeUseCase = GetWeaponsBySubTypeUseCase(),
			getWeaponByIdUseCase = GetWeaponByIdUseCase(weaponRepository)
		)
	}

	@Provides
	@Singleton
	fun provideArmorUseCases(armorRepository: ArmorRepository): ArmorUseCases {
		return ArmorUseCases(
			getLocalArmorsUseCase = GetLocalArmorsUseCase(armorRepository),
			getArmorByIdUseCase = GetArmorByIdUseCase(armorRepository),
			getArmorsBySubCategoryUseCase = GetArmorsBySubCategoryUseCase()
		)
	}


	@Provides
	@Singleton
	fun provideMeadUseCases(meadRepository: MeadRepository): MeadUseCases {
		return MeadUseCases(
			getLocalMeadsUseCase = GetLocalMeadsUseCase(meadRepository),
			getMeadsBySubCategoryUseCase = GetMeadsBySubCategoryUseCase()
		)
	}

	@Provides
	@Singleton
	fun provideToolUseCases(toolRepository: ToolRepository): ToolUseCases {
		return ToolUseCases(
			getLocalToolsUseCase = GetLocalToolsUseCase(toolRepository),
			getToolsBySubCategoryUseCase = GetToolsBySubCategoryUseCase()
		)
	}

	@Provides
	@Singleton
	fun provideBuildingMaterialUseCases(buildingMaterialRepository: BuildingMaterialRepository): BuildMaterialUseCases {
		return BuildMaterialUseCases(
			getLocalBuildMaterial = GetLocalBuildMaterialsUseCase(buildingMaterialRepository),
			getBuildMaterialByIds = GetBuildMaterialsByIdsUseCase(buildingMaterialRepository),
			getBuildMaterialById = GetBuildMaterialByIdUseCase(buildingMaterialRepository),
			getBuildMaterialsBySubCategory = GetBuildMaterialsBySubCategoryUseCase(
				buildingMaterialRepository
			),
			getBuildMaterialsBySubCategoryAndSubType = GetBuildMaterialsBySubCategoryAndSubTypeUseCase(
				buildingMaterialRepository
			),
		)
	}

	@Provides
	@Singleton
	fun provideCraftingObjectUseCases(craftingObjectRepository: CraftingObjectRepository): CraftingObjectUseCases {
		return CraftingObjectUseCases(
			getCraftingObjectByIds = GetCraftingObjectByIdsUseCase(craftingObjectRepository),
			getCraftingObjectsByIds = GetCraftingObjectsByIdsUseCase(craftingObjectRepository),
			getLocalCraftingObjectsUseCase = GetLocalCraftingObjectsUseCase(craftingObjectRepository),
			getCraftingObjectsBySubCategoryUseCase = GetCraftingObjectsBySubCategoryUseCase()
		)
	}
}