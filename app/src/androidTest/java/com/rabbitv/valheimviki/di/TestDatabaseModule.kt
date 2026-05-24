package com.rabbitv.valheimviki.di

import android.content.Context
import androidx.room.Room
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase.Companion.MIGRATION_1_2
import com.rabbitv.valheimviki.data.local.database.ValheimVikiDatabase.Companion.MIGRATION_2_3
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DatabaseModule::class])
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ValheimVikiDatabase =
        if (TestModuleConfig.useRealDatabase) {
            Room.databaseBuilder(
                context,
                ValheimVikiDatabase::class.java,
                "valheimViki_database"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
        } else {
            Room.inMemoryDatabaseBuilder(context, ValheimVikiDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        }

    @Provides fun provideSearchDao(db: ValheimVikiDatabase) = db.searchDao()
    @Provides fun provideFavoriteDao(db: ValheimVikiDatabase) = db.favoriteDao()
    @Provides fun provideBiomeDao(db: ValheimVikiDatabase) = db.biomeDao()
    @Provides fun provideCreatureDao(db: ValheimVikiDatabase) = db.creatureDao()
    @Provides fun provideRelationDao(db: ValheimVikiDatabase) = db.relationDao()
    @Provides fun provideOreDepositDao(db: ValheimVikiDatabase) = db.oreDepositDao()
    @Provides fun provideMaterialDao(db: ValheimVikiDatabase) = db.materialDao()
    @Provides fun providePointOfInterestDao(db: ValheimVikiDatabase) = db.pointOfInterestDao()
    @Provides fun provideTreeDao(db: ValheimVikiDatabase) = db.treeDao()
    @Provides fun provideFoodDao(db: ValheimVikiDatabase) = db.foodDao()
    @Provides fun provideWeaponDao(db: ValheimVikiDatabase) = db.weaponDao()
    @Provides fun provideArmorDao(db: ValheimVikiDatabase) = db.armorDao()
    @Provides fun provideMeadDao(db: ValheimVikiDatabase) = db.meadDao()
    @Provides fun provideToolDao(db: ValheimVikiDatabase) = db.toolDao()
    @Provides fun provideBuildingMaterialDao(db: ValheimVikiDatabase) = db.buildingMaterialDao()
    @Provides fun provideCraftingObjectDao(db: ValheimVikiDatabase) = db.craftingObjectDao()
    @Provides fun provideTrinketDao(db: ValheimVikiDatabase) = db.trinketDao()
}
