package com.fpa.picsum.network.RemoteMediator


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fpa.picsum.Constants.MAX_PAGE
import com.fpa.picsum.db.AppDatabase
import com.fpa.picsum.db.PageKey
import com.fpa.picsum.model.PicSum
import com.fpa.picsum.network.RetroServiceInterface


@OptIn(ExperimentalPagingApi::class)
class PicSumRemoteMediator(val service: RetroServiceInterface, val db: AppDatabase) : RemoteMediator<Int, PicSum>() {

    private val resultsrow = db.getAppDao()
    private val keyDao = db.getKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PicSum>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    val remoteKey: PageKey? = db.withTransaction {
                        if (lastItem?.id != null) {
                            keyDao.getNextPageKey(lastItem.id)
                        } else null
                    }
                    val nextPageQuery = remoteKey?.nextPageUrl?.plus(1)
                    nextPageQuery
                }
            }

            val response = service.getDataList1(loadKey?:1,size = state.config.pageSize)
            val resBody = response.body()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    resultsrow.deleteAll()
                    keyDao.clearAll()
                }
                resBody?.forEach {

                    keyDao.insertOrReplace(PageKey( 0,it.id!!,loadKey))
                }

                resBody?.let { resultsrow.insertAll(it) }

            }

            MediatorResult.Success(
                endOfPaginationReached = loadKey ==  MAX_PAGE
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}