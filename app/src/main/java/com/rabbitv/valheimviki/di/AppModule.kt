package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.remote.api.BiomeRepository
import com.rabbitv.valheimviki.data.remote.api.CreatureRepository
import com.rabbitv.valheimviki.data.repository.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.CreatureRepositoryImpl
import com.rabbitv.valheimviki.presentation.biome.BiomeViewModel
import com.rabbitv.valheimviki.presentation.creatures.CreaturesViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    ):CreatureRepository{
        return CreatureRepositoryImpl(apiService,creatureDao)
    }
    @Provides
    fun provideBiomeViewModel(biomeRepository: BiomeRepositoryImpl) = BiomeViewModel(biomeRepository)

    @Provides
    fun provideCreaturesViewModel(creatureRepository: CreatureRepositoryImpl) = CreaturesViewModel(creatureRepository)

}