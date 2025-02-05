package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.data.repository.BiomeRepositoryImpl
import com.rabbitv.valheimviki.data.repository.CreatureRepositoryImpl
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
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
        return CreatureRepositoryImpl(apiService,creatureDao)
    }

    @Singleton
    @Provides
    fun provideApiBiomeRepository(
        apiBiomeService: ApiBiomeService,
        biomeDao: BiomeDao
    ): BiomeRepository {
        return BiomeRepositoryImpl(apiBiomeService, biomeDao)
    }


    @Singleton
    @Provides
    fun provideApiCreatureRepository(
        apiCreatureService: ApiCreatureService,
        creatureDao: CreatureDao
    ): CreatureRepositoryImpl {
        return CreatureRepositoryImpl(apiCreatureService,creatureDao)
    }
}