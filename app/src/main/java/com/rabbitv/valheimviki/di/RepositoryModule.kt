package com.rabbitv.valheimviki.di

import android.content.Context
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.local.dao.RelationDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.data.repository.DataStoreOperationsImpl
import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import com.rabbitv.valheimviki.data.repository.biome.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.creatures.CreaturesRepositoryImpl
import com.rabbitv.valheimviki.data.repository.relations.RelationsRepositoryImpl
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import com.rabbitv.valheimviki.domain.repository.RelationsRepository
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_or_fetch_biomes.GetOrFetchBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_aggressive_creatures.GetAggressiveCreatures
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_creature_by_id.GetCreatureByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_creature_by_id_and_subcategory.GetCreatureByIdAndSubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_creatures_by_ids.GetCreaturesByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_bosses.GetMainBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_mini_bosses.GetMiniBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_npcs.GetNPCsUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_or_fetch_creatures.GetOrFetchCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_passive_creatures.GetPassiveCreature
import com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures.RefetchCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
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
    ): CreaturesRepository {
        return CreaturesRepositoryImpl(apiService, creatureDao)
    }

    @Provides
    @Singleton
    fun provideRelationRepositoryImpl(
        apiService: ApiRelationsService,
        relationDao: RelationDao
    ): RelationsRepository {
        return RelationsRepositoryImpl(apiService, relationDao)
    }

    @Provides
    @Singleton
    fun provideRefetchUseCase(
        biomeRepository: BiomeRepository,
        creatureRepository: CreaturesRepository,
        relationsRepository: RelationsRepository,
        dataStoreUseCases: DataStoreUseCases
    ): DataRefetchUseCase {
        return DataRefetchUseCase(
            creatureRepository = creatureRepository,
            relationsRepository = relationsRepository,
            biomeRepository = biomeRepository,
            dataStoreUseCases = dataStoreUseCases,
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
    fun provideCreatureUseCases(creatureRepository: CreaturesRepository,relationsRepository: RelationsRepository): CreatureUseCases {
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
            refetchCreaturesUseCase = RefetchCreaturesUseCase(
                creatureRepository,
                relationsRepository
            ),
        )
    }

    @Provides
    @Singleton
    fun provideRelationUseCases(relationsRepository: RelationsRepository): RelationUseCases {
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
}