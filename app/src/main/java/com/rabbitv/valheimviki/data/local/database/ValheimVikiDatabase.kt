package com.rabbitv.valheimviki.data.local.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss

@Database(entities = [Biome::class, MainBoss::class], version = 1, exportSchema = false)
abstract class ValheimVikiDatabase : RoomDatabase() {

    companion object {
        fun create(context: Context, useInMemory: Boolean): ValheimVikiDatabase {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, ValheimVikiDatabase::class.java)
            } else {
                Room.databaseBuilder(context, ValheimVikiDatabase::class.java, "valheimviki.db")
            }
            return databaseBuilder.fallbackToDestructiveMigration().build()
        }
    }

    abstract fun biomeDao(): BiomeDao
    abstract fun creatureDao(): CreatureDao
}