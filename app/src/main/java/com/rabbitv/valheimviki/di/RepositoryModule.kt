package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.repository.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.CreatureRepositoryImpl
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.biome.get_all_biomes.GetAllBiomesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_all_creatures.GetAllCreaturesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_bosses.GetBossesUseCase
import com.rabbitv.valheimviki.domain.use_cases.creatures.get_mini_bosses.GetMiniBossesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

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
    fun provideBiomeUseCases(biomeRepository: BiomeRepository): BiomeUseCases {
        return BiomeUseCases(
            getAllBiomesUseCase = GetAllBiomesUseCase(biomeRepository),
        )
    }

    @Provides
    @Singleton
    fun provideCreatureUseCases(creatureRepository: CreatureRepository): CreatureUseCases {
        return CreatureUseCases(
            getAllCreaturesUseCase = GetAllCreaturesUseCase(creatureRepository),
            getBossesUseCase = GetBossesUseCase(creatureRepository),
            getMiniBossesUseCase = GetMiniBossesUseCase(creatureRepository),
        )
    }
}