package com.rabbitv.valheimviki.di

import android.content.Context
import com.rabbitv.valheimviki.data.repository.DataStoreOperationsImpl
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
	@Provides
	@Singleton
	fun bindDataStoreOperationsImpl(
		@ApplicationContext context: Context
	): DataStoreOperations {
		return DataStoreOperationsImpl(context = context)
	}

}