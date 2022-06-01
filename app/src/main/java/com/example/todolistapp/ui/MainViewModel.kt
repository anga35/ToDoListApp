package com.example.todolistapp.ui

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.User
import com.example.todolistapp.repository.MainRepository
import com.example.todolistapp.utils.DataState
import com.example.todolistapp.utils.UserDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MainRepository,

    val savedStateHandle: SavedStateHandle
) :ViewModel() {


    val userDataState:MutableLiveData<UserDataState<User>> = MutableLiveData()



    fun loadUserData(){
        viewModelScope.launch {


            repository.getUserDataCache().collect {
                    dataState->
                    userDataState.value=dataState
            }


        }




    }





}