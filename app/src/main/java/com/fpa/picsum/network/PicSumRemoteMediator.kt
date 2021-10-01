package com.fpa.picsum.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction


import com.fpa.picsum.db.AppDatabase
import com.fpa.picsum.db.RemoteKey
import com.fpa.picsum.model.PicSum
import retrofit2.HttpException
import java.io.IOException


const val STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class PicSumRemoteMediator(
    private val api: RetroServiceInterface,
    private val db: AppDatabase
) : RemoteMediator<Int, PicSum>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, PicSum>
    ): MediatorResult {



        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {

            val response = api.getDataList(page = page,size = state.config.pageSize)

            val isEndOfList = response.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getAppDao().deleteAll()
                    db.getKeysDao().deleteAll()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.map {
                    RemoteKey(it.id.toString(), prevKey = prevKey, nextKey = nextKey)
                }
                db.getKeysDao().insertAll(keys)
                db.getAppDao().insertAll(response)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, PicSum>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PicSum>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoData ->
                db.getKeysDao().remoteKeysCatId(repoData.toString())
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, PicSum>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { repoData -> db.getKeysDao().remoteKeysCatId(repoData.id.toString()) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, PicSum>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { repoData -> db.getKeysDao().remoteKeysCatId(repoData.id.toString()) }
    }


}