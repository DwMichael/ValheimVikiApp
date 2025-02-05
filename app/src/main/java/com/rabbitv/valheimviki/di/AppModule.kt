package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.data.repository.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.CreatureRepositoryImpl
import com.rabbitv.valheimviki.presentation.biome.BiomeListScreenViewModel
import com.rabbitv.valheimviki.presentation.creatures.CreaturesViewModel
import com.rabbitv.valheimviki.presentation.detail.biome.BiomeScreenViewModel
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
    fun provideBiomeViewModel(biomeRepository: BiomeRepositoryImpl) = BiomeListScreenViewModel(biomeRepository)

    @Provides
    @Singleton
    fun provideCreaturesViewModel(creatureRepository: CreatureRepositoryImpl) = CreaturesViewModel(creatureRepository)

    @Provides
    @Singleton
    fun provideBiomeDetailViewModel(biomeRepository: BiomeRepositoryImpl) = BiomeScreenViewModel(biomeRepository = biomeRepository)

}