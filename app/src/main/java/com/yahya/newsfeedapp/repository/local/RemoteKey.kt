package com.yahya.newsfeedapp.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(@PrimaryKey val newsTitle: String, val prevKey: Int?, val nextKey: Int?)
