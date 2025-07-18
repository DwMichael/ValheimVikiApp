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
import com.rabbitv.valheimviki.data.remote.api.ApiWeaponService
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.auth_interceptor.AuthInterceptorUseCase
import com.rabbitv.valheimviki.domain.use_cases.connection.NetworkConnectivityObserver
import com.rabbitv.valheimviki.utils.ApiKey

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {


	@Provides
	@Singleton
	fun provideHttpClient(): OkHttpClient {
		return OkHttpClient.Builder()
			.connectTimeout(4, TimeUnit.SECONDS)
			.readTimeout(5, TimeUnit.SECONDS)
			.callTimeout(10, TimeUnit.SECONDS)
			.addInterceptor(AuthInterceptorUseCase())
			.build()
	}

	@Singleton
	@Provides
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl(ApiKey.BASE_URL)
			.client(okHttpClient)
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
	fun provideApiRelationService(retrofit: Retrofit): ApiRelationsService {
		return retrofit.create(ApiRelationsService::class.java)
	}

	@Singleton
	@Provides
	fun provideApiOreDepositService(retrofit: Retrofit): ApiOreDepositService {
		return retrofit.create(ApiOreDepositService::class.java)
	}

	@Singleton
	@Provides
	fun provideApiMaterialService(retrofit: Retrofit): ApiMaterialsService {
		return retrofit.create(ApiMaterialsService::class.java)
	}

	@Singleton
	@Provides
	fun provideApiPointOfInterestService(retrofit: Retrofit): ApiPointOfInterestService {
		return retrofit.create(ApiPointOfInterestService::class.java)
	}

	@Singleton
	@Provides
	fun provideApiTreeService(retrofit: Retrofit): ApiTreeService {
		return retrofit.create(ApiTreeService::class.java)
	}

	@Singleton
	@Provides
	fun provideFoodService(retrofit: Retrofit): ApiFoodService {
		return retrofit.create(ApiFoodService::class.java)
	}

	@Singleton
	@Provides
	fun provideWeaponService(retrofit: Retrofit): ApiWeaponService {
		return retrofit.create(ApiWeaponService::class.java)
	}

	@Singleton
	@Provides
	fun provideArmorService(retrofit: Retrofit): ApiArmorService {
		return retrofit.create(ApiArmorService::class.java)
	}

	@Singleton
	@Provides
	fun provideMeadService(retrofit: Retrofit): ApiMeadService {
		return retrofit.create(ApiMeadService::class.java)
	}

	@Singleton
	@Provides
	fun provideToolService(retrofit: Retrofit): ApiToolService {
		return retrofit.create(ApiToolService::class.java)
	}

	@Singleton
	@Provides
	fun provideBuildingMaterialService(retrofit: Retrofit): ApiBuildingMaterialService {
		return retrofit.create(ApiBuildingMaterialService::class.java)
	}

	@Singleton
	@Provides
	fun provideCraftingObjectService(retrofit: Retrofit): ApiCraftingService {
		return retrofit.create(ApiCraftingService::class.java)
	}

	@Provides
	@Singleton
	fun provideNetworkConnectivityObserver(
		@ApplicationContext context: Context
	): NetworkConnectivity {
		return NetworkConnectivityObserver(context = context)
	}

}