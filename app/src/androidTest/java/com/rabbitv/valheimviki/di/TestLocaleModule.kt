package com.rabbitv.valheimviki.di

import com.rabbitv.valheimviki.boot.DefaultLocaleProvider
import com.rabbitv.valheimviki.boot.FakeLocaleProvider
import com.rabbitv.valheimviki.boot.LocaleProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [LocaleModule::class])
object TestLocaleModule {

    @Provides
    @Singleton
    fun provideFakeLocale(): FakeLocaleProvider = FakeLocaleProvider()

    @Provides
    @Singleton
    fun provideLocaleProvider(fake: FakeLocaleProvider): LocaleProvider =
        if (TestModuleConfig.useRealLocaleProvider) DefaultLocaleProvider() else fake
}
