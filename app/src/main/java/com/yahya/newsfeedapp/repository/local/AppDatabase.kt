package com.yahya.newsfeedapp.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yahya.newsfeedapp.repository.remote.NewsListModel

@Database(version = 1, entities = [NewsListModel::class, RemoteKey::class], exportSchema = false)
abstract class AppDatabase: RoomDatabase()  {

    abstract fun getRepoDao(): RemoteKeysDao
    abstract fun getNewsModelDao(): NewsDAO

    companion object {

        val NEWS_DB = "news.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, NEWS_DB)
                .build()
    }
}