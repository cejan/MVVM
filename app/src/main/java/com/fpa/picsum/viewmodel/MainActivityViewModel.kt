package com.fpa.picsum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fpa.picsum.network.RetroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(repository: RetroRepository)
    : ViewModel() {

    val listData = repository.getDataFromPagingSource().flow.cachedIn(viewModelScope)

    @ExperimentalPagingApi
     val dataSource = repository.getDataFromMediator()

}