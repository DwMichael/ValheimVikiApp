package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.boot.DefaultLocaleProvider
import com.rabbitv.valheimviki.boot.LocaleProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocaleModule {
	@Binds
	@Singleton
	abstract fun bindLocaleProvider(impl: DefaultLocaleProvider): LocaleProvider
}
