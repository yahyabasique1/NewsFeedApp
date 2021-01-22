package com.yahya.newsfeedapp.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remotekey WHERE newsTitle = :title")
    suspend fun remoteKeysNewsTitle(title: String): RemoteKey?

    @Query("DELETE FROM remotekey")
    suspend fun clearRemoteKeys()
}

