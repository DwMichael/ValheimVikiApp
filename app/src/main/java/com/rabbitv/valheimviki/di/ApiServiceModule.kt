package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.repository.ApiBiomeRepository
import com.rabbitv.valheimviki.data.repository.ApiCreatureRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    private const val BASE_URL = "http://192.168.1.130:8100/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiBiomeService(retrofit: Retrofit): ApiBiomeService {
        return retrofit.create(ApiBiomeService::class.java)
    }

    @Singleton
    @Provides
    fun provideApiCreatureService(retrofit: Retrofit): ApiCreatureService {
        return retrofit.create(ApiCreatureService::class.java)
    }

    @Singleton
    @Provides
    fun provideApiBiomeRepository(apiBiomeService: ApiBiomeService): ApiBiomeRepository {
        return ApiBiomeRepository(apiBiomeService)
    }

    @Singleton
    @Provides
    fun provideApiCreatureRepository(apiCreatureService: ApiCreatureService): ApiCreatureRepository {
        return ApiCreatureRepository(apiCreatureService)
    }
}