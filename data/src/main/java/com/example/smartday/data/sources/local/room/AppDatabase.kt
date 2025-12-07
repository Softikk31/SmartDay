package com.example.smartday.data.sources.local.room

import ConvertersRepetition
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartday.data.sources.local.converters.task.ConvertersDateTime
import com.example.smartday.data.sources.local.converters.task.ConvertersPriority
import com.example.smartday.data.sources.local.converters.theme.ConvertersPrimaryColor
import com.example.smartday.data.sources.local.entities.TaskEntity
import com.example.smartday.data.sources.local.entities.ThemeEntity

@Database(
    entities = [TaskEntity::class, ThemeEntity::class], version = 1, exportSchema = true
)
@TypeConverters(
    ConvertersDateTime::class, ConvertersPriority::class, ConvertersRepetition::class,
    ConvertersPrimaryColor::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun themeDao(): ThemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "Database.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}