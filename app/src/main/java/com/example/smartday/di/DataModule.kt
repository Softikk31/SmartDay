package com.example.smartday.di

import androidx.room.Room
import com.example.smartday.data.repository.TaskRepositoryImpl
import com.example.smartday.data.sources.TaskLocalSource
import com.example.smartday.data.sources.local.TaskLocalSourceImpl
import com.example.smartday.data.sources.local.room.AppDatabase
import com.example.smartday.data.sources.local.room.TaskDao
import com.example.smartday.domain.repository.TaskRepository
import org.koin.dsl.module


val dataModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "smartday_database"
        ).build()
    }

    single<TaskDao> { get<AppDatabase>().taskDao() }

    single<TaskLocalSource> {
        TaskLocalSourceImpl(get())
    }

    single<TaskRepository> {
        TaskRepositoryImpl(get())
    }
}