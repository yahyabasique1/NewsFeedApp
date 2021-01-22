package com.yahya.newsfeedapp.repository.local

import androidx.paging.PagingSource
import androidx.room.*
import com.google.android.material.textfield.TextInputLayout
import com.yahya.newsfeedapp.repository.remote.NewsListModel

@Dao
interface NewsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newsListModel: List<NewsListModel>)

    @Query("SELECT * FROM newslistmodel")
     fun getAllNewsModel(): PagingSource<Int, NewsListModel>

    @Query("DELETE FROM newslistmodel")
    suspend fun clearAllNews()

}