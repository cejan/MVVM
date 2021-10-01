package com.fpa.picsum.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val dataId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
