package com.fpa.picsum.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fpa.picsum.model.PicSum
import com.fpa.picsum.network.RetroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewActivityViewModel @Inject constructor(private val repository: RetroRepository)
    : ViewModel() {

    fun getdata(id: String): LiveData<PicSum> {
        return repository.getOneRecord(id)
    }

}