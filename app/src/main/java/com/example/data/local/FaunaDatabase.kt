package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SpeciesScanEntity::class], version = 1, exportSchema = false)
abstract class FaunaDatabase : RoomDatabase() {
    abstract fun speciesScanDao(): SpeciesScanDao

    companion object {
        @Volatile
        private var INSTANCE: FaunaDatabase? = null

        fun getInstance(context: Context): FaunaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FaunaDatabase::class.java,
                    "faunalens.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
