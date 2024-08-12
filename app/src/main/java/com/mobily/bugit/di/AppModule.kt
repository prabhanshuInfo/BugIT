package com.mobily.bugit.di

import android.content.Context
import androidx.room.Room
import com.mobily.bugit.database.AppDatabase
import com.mobily.bugit.database.dao.BugDao
import com.mobily.bugit.domain.BugRepositoryImpl
import com.mobily.bugit.domain.repo.BugRepository
import com.mobily.bugit.domain.useCases.FetchBugUseCase
import com.mobily.bugit.domain.useCases.InsertBugUseCase
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
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "bug_database").build()
    }

    @Provides
    @Singleton
    fun provideBugDao(appDatabase: AppDatabase): BugDao {
        return appDatabase.bugDao()
    }

    @Provides
    @Singleton
    fun provideBugRepository(
        bugDao: BugDao
    ): BugRepository {
        return BugRepositoryImpl(bugDao)
    }

    @Provides
    @Singleton
    fun provideGetALlBugs(repository: BugRepository): FetchBugUseCase {
        return FetchBugUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideInsertBugUseCase(repository: BugRepository): InsertBugUseCase {
        return InsertBugUseCase(repository)
    }
}