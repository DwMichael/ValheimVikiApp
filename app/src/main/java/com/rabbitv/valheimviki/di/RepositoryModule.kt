package com.rabbitv.valheimviki.di

import android.content.Context
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.local.dao.FoodDao
import com.rabbitv.valheimviki.data.local.dao.MaterialDao
import com.rabbitv.valheimviki.data.local.dao.OreDepositDao
import com.rabbitv.valheimviki.data.local.dao.PointOfInterestDao
import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.local.dao.TreeDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.remote.api.ApiFoodService
import com.rabbitv.valheimviki.data.remote.api.ApiMaterialsService
import com.rabbitv.valheimviki.data.remote.api.ApiOreDepositService
import com.rabbitv.valheimviki.data.remote.api.ApiPointOfInterestService
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.data.remote.api.ApiTreeService
import com.rabbitv.valheimviki.data.repository.DataStoreOperationsImpl
import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import com.rabbitv.valheimviki.data.repository.biome.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.creature.CreatureRepositoryImpl
import com.rabbitv.valheimviki.data.repository.food.FoodRepositoryImpl
import com.rabbitv.valheimviki.data.repository.material.MaterialRepositoryImpl
import com.rabbitv.valheimviki.data.repository.ore_deposit.OreDepositRepositoryImpl
import com.rabbitv.valheimviki.data.repository.point_of_interest.PointOfInterestRepositoryImpl
import com.rabbitv.valheimviki.data.repository.relation.RelationRepositoryImpl
import com.rabbitv.valheimviki.data.repository.tree.TreeRepositoryImpl
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_local_biomes.GetLocalBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.connection.NetworkConnectivityObserver
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
import com.rabbitv.valheimviki.domain.use_cases.creature.refetch_creatures.RefetchCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
import com.rabbitv.valheimviki.domain.use_cases.food.FoodUseCases
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_ids.GetFoodListByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_food_list_by_subCategory.GetFoodListBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.food.get_local_food_list.GetLocalFoodListUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.MaterialUseCases
import com.rabbitv.valheimviki.domain.use_cases.material.get_local_Materials.GetLocalMaterialsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_material_by_id.GetMaterialByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_ids.GetMaterialsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory.GetMaterialsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.get_materials_by_subcategory_and_subtype.GetMaterialsBySubCategoryAndSubTypeUseCase
import com.rabbitv.valheimviki.domain.use_cases.material.insert_materials.InsertMaterialsUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit.GetLocalOreDepositUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id.GetOreDepositByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids.GetOreDepositsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.insert_ore_deposit.InsertOreDepositUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_local_point_of_interest.GetLocalPointOfInterestUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_id.GetPointOfInterestByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory.GetPointsOfInterestBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory_and_id.GetPointOfInterestBySubCategoryAndIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interests_by_ids.GetPointsOfInterestByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.insert_point_of_interest.InsertPointOfInterestUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation.GetRelatedIdsRelationUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_related_by_id.GetItemRelatedById
import com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations.GetLocalRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.insert_relations.InsertRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.TreeUseCases
import com.rabbitv.valheimviki.domain.use_cases.tree.get_local_trees.GetLocalTreesUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_tree_by_id.GetTreeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_trees_by_ids.GetTreesByIdsUseCase
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
            foodRepository = foodRepository
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
        relationsRepository: RelationRepository
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
            refetchCreaturesUseCase = RefetchCreaturesUseCase(
                creatureRepository,
                relationsRepository
            ),
        )
    }

    @Provides
    @Singleton
    fun provideRelationUseCases(relationsRepository: RelationRepository): RelationUseCases {
        return RelationUseCases(
            insertRelationsUseCase = InsertRelationsUseCase(relationsRepository),
            getRelatedIdUseCase = GetItemRelatedById(relationsRepository),
            getRelatedIdsUseCase = GetRelatedIdsRelationUseCase(relationsRepository),
            getLocalRelationsUseCase = GetLocalRelationsUseCase(relationsRepository),
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
            insertOreDepositUseCase = InsertOreDepositUseCase(oreDepositRepository),
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
            ),
            insertMaterials = InsertMaterialsUseCase(materialRepository)
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
            ),
            insertPointOfInterestUseCase = InsertPointOfInterestUseCase(pointOfInterestRepository)
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
            getFoodListByIdsUseCase = GetFoodListByIdsUseCase(foodRepository)
        )
    }
}