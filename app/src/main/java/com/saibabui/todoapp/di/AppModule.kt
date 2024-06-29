package com.saibabui.todoapp.di

import android.content.Context
import androidx.room.Room
import com.saibabui.todoapp.data.TaskDao
import com.saibabui.todoapp.data.TaskDatabase
import com.saibabui.todoapp.domain.repository.TodoRepository
import com.saibabui.todoapp.data.repository.TodoRepositoryImpl
import com.saibabui.todoapp.ui.presentation.add.AddTodoViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            "task_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideChannelDao(appDatabase: TaskDatabase): TaskDao {
        return appDatabase.taskDao()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(
        db: TaskDao
    ): TodoRepository = TodoRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideAddTodoViewModel(todoRepository: TodoRepository) : AddTodoViewModel = AddTodoViewModel(todoRepository)
}
