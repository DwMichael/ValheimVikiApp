package com.rabbitv.valheimviki.di

import android.content.Context
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.local.dao.MaterialDao
import com.rabbitv.valheimviki.data.local.dao.OreDepositDao
import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.remote.api.ApiMaterialsService
import com.rabbitv.valheimviki.data.remote.api.ApiOreDepositService
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.data.repository.DataStoreOperationsImpl
import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import com.rabbitv.valheimviki.data.repository.biome.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.creature.CreatureRepositoryImpl
import com.rabbitv.valheimviki.data.repository.material.MaterialRepositoryImpl
import com.rabbitv.valheimviki.data.repository.ore_deposit.OreDepositRepositoryImpl
import com.rabbitv.valheimviki.data.repository.relation.RelationRepositoryImpl
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_or_fetch_biomes.GetOrFetchBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.creature.get_aggressive_creatures.GetAggressiveCreatures
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id.GetCreatureByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_id_and_subcategory.GetCreatureByIdAndSubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creature_by_relation_and_sub_category.GetCreatureByRelationAndSubCategory
import com.rabbitv.valheimviki.domain.use_cases.creature.get_creatures_by_ids.GetCreaturesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_main_bosses.GetMainBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_mini_bosses.GetMiniBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_npcs.GetNPCsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_or_fetch_creatures.GetOrFetchCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creature.get_passive_creatures.GetPassiveCreature
import com.rabbitv.valheimviki.domain.use_cases.creature.refetch_creatures.RefetchCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
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
import com.rabbitv.valheimviki.domain.use_cases.relation.RelationUseCases
import com.rabbitv.valheimviki.domain.use_cases.relation.fetch_relations.FetchRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.fetch_relations_and_insert.FetchRelationsAndInsertUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_id_in_relation.GetRelatedIdsRelationUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.get_item_related_by_id.GetItemRelatedById
import com.rabbitv.valheimviki.domain.use_cases.relation.get_local_relations.GetLocalRelationsUseCase
import com.rabbitv.valheimviki.domain.use_cases.relation.insert_relations.InsertRelationsUseCase
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
    fun provideRefetchUseCase(
        biomeRepository: BiomeRepository,
        creatureRepository: CreatureRepository,
        relationsRepository: RelationRepository,
        materialRepository: MaterialRepository,
        oreDepositRepository: OreDepositRepository,
        dataStoreUseCases: DataStoreUseCases
    ): DataRefetchUseCase {
        return DataRefetchUseCase(
            creatureRepository = creatureRepository,
            relationsRepository = relationsRepository,
            biomeRepository = biomeRepository,
            oreDepositRepository = oreDepositRepository,
            dataStoreUseCases = dataStoreUseCases,
            materialsRepository =  materialRepository
        )
    }

    @Provides
    @Singleton
    fun provideBiomeUseCases(biomeRepository: BiomeRepository): BiomeUseCases {
        return BiomeUseCases(
            getOrFetchBiomesUseCase = GetOrFetchBiomesUseCase(biomeRepository),
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
            getOrFetchCreaturesUseCase = GetOrFetchCreaturesUseCase(creatureRepository),
            getCreatureByRelationAndSubCategory = GetCreatureByRelationAndSubCategory(
                creatureRepository
            ),
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
            fetchRelationsUseCase = FetchRelationsUseCase(relationsRepository),
            insertRelationsUseCase = InsertRelationsUseCase(relationsRepository),
            getRelatedIdUseCase = GetItemRelatedById(relationsRepository),
            getRelatedIdsUseCase = GetRelatedIdsRelationUseCase(relationsRepository),
            getLocalRelationsUseCase = GetLocalRelationsUseCase(relationsRepository),
            fetchRelationsAndInsertUseCase = FetchRelationsAndInsertUseCase(relationsRepository),
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
    fun provideMaterialUseCases(materialRepository: MaterialRepository): MaterialUseCases{
        return MaterialUseCases(
            getLocalMaterials = GetLocalMaterialsUseCase(materialRepository),
            getMaterialsByIds = GetMaterialsByIdsUseCase(materialRepository),
            getMaterialById = GetMaterialByIdUseCase(materialRepository),
            getMaterialsBySubCategory = GetMaterialsBySubCategoryUseCase(materialRepository),
            getMaterialsBySubCategoryAndSubType = GetMaterialsBySubCategoryAndSubTypeUseCase(materialRepository),
            insertMaterials = InsertMaterialsUseCase(materialRepository)
        )
    }
}