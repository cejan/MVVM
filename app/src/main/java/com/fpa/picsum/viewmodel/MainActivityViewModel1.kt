package com.fpa.picsum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fpa.picsum.network.PicSumPagingSource
import com.fpa.picsum.network.RetroRepository
import com.fpa.picsum.network.RetroServiceInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel1 @Inject constructor(repository: RetroRepository, private val retroServiceInterface: RetroServiceInterface)
    : ViewModel() {


    val listData = Pager(PagingConfig(pageSize = 30)) {
        PicSumPagingSource(retroServiceInterface)
    }.flow.cachedIn(viewModelScope)


    @ExperimentalPagingApi
     val dataSource = repository.getCatsFromMediator()

}