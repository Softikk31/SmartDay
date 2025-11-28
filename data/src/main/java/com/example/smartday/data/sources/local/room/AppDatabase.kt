package com.example.smartday.data.sources.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartday.data.sources.local.converters.ConvertersDateTime
import com.example.smartday.data.sources.local.converters.ConvertersPriority
import com.example.smartday.data.sources.local.entities.TaskEntity

@Database(
    entities = [TaskEntity::class], version = 1, exportSchema = true
)
@TypeConverters(ConvertersDateTime::class, ConvertersPriority::class, ConvertersRepetition::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}