package com.rabbitv.valheimviki.di

import android.content.Context
import com.rabbitv.valheimviki.data.remote.api.ApiArmorService
import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.data.remote.api.ApiBuildingMaterialService
import com.rabbitv.valheimviki.data.remote.api.ApiCraftingService
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.data.remote.api.ApiFoodService
import com.rabbitv.valheimviki.data.remote.api.ApiMaterialsService
import com.rabbitv.valheimviki.data.remote.api.ApiMeadService
import com.rabbitv.valheimviki.data.remote.api.ApiOreDepositService
import com.rabbitv.valheimviki.data.remote.api.ApiPointOfInterestService
import com.rabbitv.valheimviki.data.remote.api.ApiRelationsService
import com.rabbitv.valheimviki.data.remote.api.ApiToolService
import com.rabbitv.valheimviki.data.remote.api.ApiTreeService
import com.rabbitv.valheimviki.data.remote.api.ApiTrinketService
import com.rabbitv.valheimviki.data.remote.api.ApiWeaponService
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.connection.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [NetWorkModule::class])
object TestNetworkModule {

    @Provides
    @Singleton
    fun provideMockWebServer(): MockWebServer {
        val server = MockWebServer()
        server.start()
        return server
    }

    @Provides
    @Singleton
    fun provideTestOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideTestRetrofit(mockWebServer: MockWebServer, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideApiBiomeService(r: Retrofit): ApiBiomeService = r.create(ApiBiomeService::class.java)

    @Provides @Singleton
    fun provideApiCreatureService(r: Retrofit): ApiCreatureService = r.create(ApiCreatureService::class.java)

    @Provides @Singleton
    fun provideApiRelationService(r: Retrofit): ApiRelationsService = r.create(ApiRelationsService::class.java)

    @Provides @Singleton
    fun provideApiOreDepositService(r: Retrofit): ApiOreDepositService = r.create(ApiOreDepositService::class.java)

    @Provides @Singleton
    fun provideApiMaterialService(r: Retrofit): ApiMaterialsService = r.create(ApiMaterialsService::class.java)

    @Provides @Singleton
    fun provideApiPointOfInterestService(r: Retrofit): ApiPointOfInterestService = r.create(ApiPointOfInterestService::class.java)

    @Provides @Singleton
    fun provideApiTreeService(r: Retrofit): ApiTreeService = r.create(ApiTreeService::class.java)

    @Provides @Singleton
    fun provideFoodService(r: Retrofit): ApiFoodService = r.create(ApiFoodService::class.java)

    @Provides @Singleton
    fun provideWeaponService(r: Retrofit): ApiWeaponService = r.create(ApiWeaponService::class.java)

    @Provides @Singleton
    fun provideArmorService(r: Retrofit): ApiArmorService = r.create(ApiArmorService::class.java)

    @Provides @Singleton
    fun provideMeadService(r: Retrofit): ApiMeadService = r.create(ApiMeadService::class.java)

    @Provides @Singleton
    fun provideToolService(r: Retrofit): ApiToolService = r.create(ApiToolService::class.java)

    @Provides @Singleton
    fun provideBuildingMaterialService(r: Retrofit): ApiBuildingMaterialService = r.create(ApiBuildingMaterialService::class.java)

    @Provides @Singleton
    fun provideCraftingObjectService(r: Retrofit): ApiCraftingService = r.create(ApiCraftingService::class.java)

    @Provides @Singleton
    fun provideTrinketService(r: Retrofit): ApiTrinketService = r.create(ApiTrinketService::class.java)

    @Provides @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity =
        NetworkConnectivityObserver(context = context)
}
