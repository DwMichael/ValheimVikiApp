package com.rabbitv.valheimviki.di

import android.app.Application
import androidx.room.Room
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): ValheimVikiDatabase {
        return Room.databaseBuilder(
            application,
            ValheimVikiDatabase::class.java,
            "valheimViki_database"
        )
            .fallbackToDestructiveMigration()//for migration only
            .build()
    }

    @Provides
    fun provideBiomeDao(appDatabase: ValheimVikiDatabase) = appDatabase.biomeDao()

    @Provides
    fun provideCreatureDao(appDatabase: ValheimVikiDatabase) = appDatabase.creatureDao()

    @Provides
    fun provideRelationDao(appDatabase: ValheimVikiDatabase) = appDatabase.relationDao()

    @Provides
    fun provideOreDepositDao(appDatabase: ValheimVikiDatabase) = appDatabase.oreDepositDao()

    @Provides
    fun provideMaterialDao(appDatabase: ValheimVikiDatabase) = appDatabase.materialDao()

    @Provides
    fun providePointOfInterestDao(appDatabase: ValheimVikiDatabase) =
        appDatabase.pointOfInterestDao()

    @Provides
    fun provideTreeDao(appDatabase: ValheimVikiDatabase) = appDatabase.treeDao()

    @Provides
    fun provideFoodDao(appDatabase: ValheimVikiDatabase) = appDatabase.foodDao()
}