package com.aigak.gifviewer.viewmodel

import androidx.lifecycle.viewModelScope
import com.aigak.gifviewer.networking.api.response.Data
import com.aigak.gifviewer.networking.api.response.GifSearchResponse
import com.aigak.gifviewer.repository.GifSearchRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class GifSearchViewModel(private val repository: GifSearchRepository) : ParentViewModel() {
    private val _gifSearchLiveData =
        androidx.lifecycle.MutableLiveData<List<GifSearchResponse?>?>()
    val gifSearchLiveData: androidx.lifecycle.LiveData<List<GifSearchResponse?>?> get() = _gifSearchLiveData

    private val _errorLiveData =
        androidx.lifecycle.MutableLiveData<Throwable?>()
    override val errorLiveData: androidx.lifecycle.LiveData<Throwable?> get() = _errorLiveData

    private val _searchKeyword =
        androidx.lifecycle.MutableLiveData("cat")

    val searchKeyword: androidx.lifecycle.LiveData<String> get() = _searchKeyword

    private val _offset =
        androidx.lifecycle.MutableLiveData(0)

    val offset:
            androidx.lifecycle.MutableLiveData<Int>
        get() = _offset

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _errorLiveData.value = exception
        isLoading.value = false
    }

    private var searchJob: Job? = null
    var isLoading = androidx.lifecycle.MutableLiveData<Boolean>(false)


    fun searchGifs(searchKeyword: String? = _searchKeyword.value, delayTime: Long = 1500) {
        searchJob?.cancel()
        if (searchKeyword.isNullOrBlank()) {
            return
        }
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(delayTime)
            search(searchKeyword)

        }
    }

    suspend fun search(searchKeyword: String) {
        if (_searchKeyword.value != searchKeyword) {
            _gifSearchLiveData.value = mutableListOf()
            _offset.value = 0
        } else {
            _gifSearchLiveData.value?.forEach { dataItem ->
                if (dataItem?.pagination?.offset == _offset.value) {
                    // data list contains this search item and offset
                    return
                }
            }
        }
        _searchKeyword.value = searchKeyword
        isLoading.value = true
        val response =
            repository.searchGifs(
                searchKeyword = searchKeyword,
                offset = _offset.value ?: 0
            )
        if (response.second != null) {
            _errorLiveData.value = response.second
            return
        }

        val oldList = _gifSearchLiveData.value?.toMutableList() ?: mutableListOf()
        val oldOffset = _offset.value
        _offset.value = (oldOffset ?: 0) + (response.first?.pagination?.count ?: 0)
        oldList.add(response.first)
        _gifSearchLiveData.value = oldList
        isLoading.value = false
    }
}
