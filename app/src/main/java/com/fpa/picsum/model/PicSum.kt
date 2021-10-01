package com.fpa.picsum.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picSum")
data class PicSum(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "download_url")
    val download_url: String?
    )


