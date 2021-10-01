package com.fpa.picsum.network


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fpa.picsum.model.PicSum


class PicSumPagingSource
        (
        private val apiService: RetroServiceInterface
    ) : PagingSource<Int, PicSum>() {

        override fun getRefreshKey(state: PagingState<Int, PicSum>): Int? {

            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }

        }

        override suspend fun load(params: LoadParams<Int>):
                LoadResult<Int, PicSum> {

            return try {
                val currentPage = params.key ?: 1
                val response = apiService.getDataFromAPI(currentPage, size = params.loadSize)
                val responseData = mutableListOf<PicSum>()
                val data = response.body() ?: emptyList()
                responseData.addAll(data)

                LoadResult.Page(
                    data = responseData,
                    prevKey = if (currentPage == 1) null else -1,
                    nextKey = if (responseData.isEmpty()) null else currentPage + 1

                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }

        }
    }