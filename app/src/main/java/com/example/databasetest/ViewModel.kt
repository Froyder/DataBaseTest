package com.example.databasetest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.databasetest.database.User

class ViewModel(private val liveDataToObserve: MutableLiveData<User> = MutableLiveData()) :
    ViewModel() {

    fun getData() = liveDataToObserve

    fun addUser (newUser : User) {
        liveDataToObserve.postValue(newUser)
    }
}