package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.data.repository.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.CreatureRepositoryImpl
import com.rabbitv.valheimviki.presentation.biome.BiomeGridScreenViewModel
import com.rabbitv.valheimviki.presentation.creatures.CreaturesViewModel
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossesViewModel
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossesViewModel
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
    fun provideBossesViewModel(creatureRepository: CreatureRepositoryImpl) =
        BossesViewModel(creatureRepository)

    @Provides
    @Singleton
    fun provideMiniBossesViewModel(creatureRepository: CreatureRepositoryImpl) =
        MiniBossesViewModel(creatureRepository)

    @Provides
    @Singleton
    fun provideBiomeViewModel(biomeRepository: BiomeRepositoryImpl) =
        BiomeGridScreenViewModel(biomeRepository)

    @Provides
    @Singleton
    fun provideCreaturesViewModel(creatureRepository: CreatureRepositoryImpl) =
        CreaturesViewModel(creatureRepository)

    @Provides
    @Singleton
    fun provideBiomeDetailViewModel(biomeRepository: BiomeRepositoryImpl) =
        BiomeScreenViewModel(biomeRepository = biomeRepository)

}