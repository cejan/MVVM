package com.fpa.picsum.network

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.fpa.picsum.Constants.PAGE_SIZE
import com.fpa.picsum.db.AppDao
import com.fpa.picsum.db.AppDatabase
import com.fpa.picsum.model.PicSum
import com.fpa.picsum.network.PagingSource.PicSumPagingSource
import com.fpa.picsum.network.RemoteMediator.PicSumRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//private const val PAGE_SIZE = 30

class RetroRepository @Inject constructor(private val retroServiceInterface: RetroServiceInterface,
                                          private val appDao: AppDao, private val database: AppDatabase
) {

    fun getDataFromPagingSource(): Pager<Int,PicSum> {
       return Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            PicSumPagingSource(retroServiceInterface)
        }
    }


    @ExperimentalPagingApi
    fun getDataFromMediator(): Flow<PagingData<PicSum>> {
        val pagingSourceFactory = { appDao.getAll() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            remoteMediator = PicSumRemoteMediator(
                retroServiceInterface,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getOneRecord(id: String): LiveData<PicSum> {
        return appDao.getRecordById(id)
    }



}