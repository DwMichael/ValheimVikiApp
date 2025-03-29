package com.rabbitv.valheimviki.di

import android.content.Context
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.repository.DataStoreOperationsImpl
import com.rabbitv.valheimviki.data.repository.DataStoreRepository
import com.rabbitv.valheimviki.data.repository.biome.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.creatures.CreaturesRepositoryImpl
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes.GetAllBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.get_biome_by_id.GetBiomeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.biome.refetch_biomes.RefetchBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_boss_by_id.GetMainBossByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_main_bosses.GetMainBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.refetch_creatures.RefetchCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
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
    fun provideBiomeUseCases(biomeRepository: BiomeRepository): BiomeUseCases {
        return BiomeUseCases(
            getAllBiomesUseCase = GetAllBiomesUseCase(biomeRepository),
            getBiomeByIdUseCase = GetBiomeByIdUseCase(biomeRepository),
            refetchBiomesUseCase = RefetchBiomesUseCase(biomeRepository)
        )
    }

    @Provides
    @Singleton
    fun provideCreatureUseCases(creatureRepository: CreaturesRepository): CreatureUseCases {
        return CreatureUseCases(
            getMainBossesUseCase = GetMainBossesUseCase(creatureRepository),
            getMainBossesByIdUseCase = GetMainBossByIdUseCase(creatureRepository),
            refetchCreaturesUseCase = RefetchCreaturesUseCase(creatureRepository)
        )
    }

    @Provides
    @Singleton
    fun provideDataStoreUseCases(dataStoreRepository: DataStoreRepository): DataStoreUseCases {
        return DataStoreUseCases(
            saveOnBoardingState = SaveOnBoardingState(dataStoreRepository),
            readOnBoardingUseCase = ReadOnBoardingState(dataStoreRepository),
        )
    }
}