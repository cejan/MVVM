package com.fpa.picsum.network

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.fpa.picsum.db.AppDao
import com.fpa.picsum.db.AppDatabase
import com.fpa.picsum.model.PicSum
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
private const val PAGE_SIZE = 100
class RetroRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface,
                                          private val appDao: AppDao, private val database: AppDatabase
) {

    @ExperimentalPagingApi
    fun getCatsFromMediator(): Flow<PagingData<PicSum>> {
        val pagingSourceFactory = { appDao.getAll() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false,
            ),
            remoteMediator = PicSumRemoteMediator(
                retroServiceInterface,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getOneRecord(id: Int): LiveData<PicSum> {
        return appDao.getRecordById(id)

    }



}