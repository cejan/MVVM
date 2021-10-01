package com.fpa.picsum.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fpa.picsum.model.PicSum

@Dao
interface AppDao {

    @Query("SELECT * FROM picSum")
    fun getAllRecords(): LiveData<List<PicSum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecords(PicSum: PicSum)

    @Query("SELECT * FROM picSum where id = :id")
    fun getRecordById(id: Int): LiveData<PicSum>

    @Query("DELETE FROM picSum")
    suspend fun deleteAll()

    @Query("SELECT * FROM picSum")
    fun getAll(): PagingSource<Int, PicSum>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repo: List<PicSum>)



}