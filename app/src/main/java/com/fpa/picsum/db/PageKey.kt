package com.fpa.picsum.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pageKey")
data class PageKey(
    @PrimaryKey(autoGenerate = true)
    val id1: Int = 0,
    val id: String,
    val nextPageUrl: Int?
)
