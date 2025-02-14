package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.domain.use_cases.biome.BiomeUseCases
import com.rabbitv.valheimviki.domain.use_cases.creatures.CreatureUseCases
import com.rabbitv.valheimviki.presentation.biome.BiomeScreenViewModel
import com.rabbitv.valheimviki.presentation.creatures.CreaturesViewModel
import com.rabbitv.valheimviki.presentation.creatures.bosses.BossesViewModel
import com.rabbitv.valheimviki.presentation.creatures.mini_bosses.MiniBossesViewModel
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
    fun provideBossesViewModel(creatureUseCases: CreatureUseCases) =
        BossesViewModel(creatureUseCases)

    @Provides
    @Singleton
    fun provideMiniBossesViewModel(creatureUseCases: CreatureUseCases) =
        MiniBossesViewModel(creatureUseCases)

    @Provides
    @Singleton
    fun provideBiomeViewModel(biomesUseCase: BiomeUseCases) =
        BiomeScreenViewModel(biomesUseCase)

    @Provides
    @Singleton
    fun provideCreaturesViewModel(creatureUseCases: CreatureUseCases) =
        CreaturesViewModel(creatureUseCases)

//
//    @Provides
//    @Singleton
//    fun provideBiomeDetailViewModel(biomeRepository: BiomeRepositoryImpl) =
//        BiomeScreenViewModel(biomeRepository = biomeRepository)

}