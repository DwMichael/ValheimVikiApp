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

}