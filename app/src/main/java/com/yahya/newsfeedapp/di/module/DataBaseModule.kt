package com.yahya.newsfeedapp.di.module

import android.content.Context
import android.provider.DocumentsContract
import androidx.room.Room
import com.yahya.newsfeedapp.di.ApplicationContext
import com.yahya.newsfeedapp.di.DatabaseInfo
import com.yahya.newsfeedapp.repository.local.AppDatabase
import com.yahya.newsfeedapp.repository.local.NewsDAO
import com.yahya.newsfeedapp.repository.local.RemoteKeysDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule(private var context: Context) {



    @DatabaseInfo
    var databaseName="newsfeed.db"




    @Provides
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName
        ).allowMainThreadQueries()
            .build()
    }

    @Provides
    @DatabaseInfo
    fun ovideDatabseName():String{
        return databaseName
    }


    @Singleton
    @Provides
    fun provideRemoteDao(appDatabase: AppDatabase):RemoteKeysDao{
        return appDatabase.getRepoDao()
    }

    @Singleton
    @Provides
    fun provideNewsDao(appDatabase: AppDatabase):NewsDAO{
        return appDatabase.getNewsModelDao()
    }




}