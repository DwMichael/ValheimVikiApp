package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.di.qualifiers.DefaultDispatcher
import com.rabbitv.valheimviki.di.qualifiers.IoDispatcher
import com.rabbitv.valheimviki.di.qualifiers.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {


	@Provides
	@IoDispatcher
	fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

	@Provides
	@DefaultDispatcher
	fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

	@Provides
	@MainDispatcher
	fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}