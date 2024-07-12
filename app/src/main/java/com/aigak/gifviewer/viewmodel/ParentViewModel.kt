package com.aigak.gifviewer.viewmodel

import androidx.lifecycle.ViewModel

abstract class ParentViewModel : ViewModel() {
    abstract val errorLiveData: androidx.lifecycle.LiveData<Throwable?>
}