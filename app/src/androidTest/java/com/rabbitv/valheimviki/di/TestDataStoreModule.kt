package com.rabbitv.valheimviki.di

import android.content.Context
import com.rabbitv.valheimviki.data.repository.DataStoreOperationsImpl
import com.rabbitv.valheimviki.data.repository.FakeDataStoreOperations
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataStoreModule::class])
object TestDataStoreModule {

    @Provides
    @Singleton
    fun provideFakeDataStore(): FakeDataStoreOperations = FakeDataStoreOperations()

    @Provides
    @Singleton
    fun provideDataStoreOperations(
        @ApplicationContext context: Context,
        fake: FakeDataStoreOperations,
    ): DataStoreOperations =
        if (TestModuleConfig.useRealDataStore) DataStoreOperationsImpl(context = context)
        else fake
}
