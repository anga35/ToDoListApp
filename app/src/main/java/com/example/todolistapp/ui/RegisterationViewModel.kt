package com.example.todolistapp.ui

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.repository.MainRepository
import com.example.todolistapp.retrofit.dto.SignupDTO
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.awaitResponse
import javax.inject.Inject


@HiltViewModel
class RegisterationViewModel
@Inject
constructor(
    private val repository: MainRepository,
    private val savedStateHandle:SavedStateHandle
):ViewModel() {
    var controller:MutableLiveData<Response<Any>> = MutableLiveData()



    fun createUser(signupDTO: SignupDTO) {

        viewModelScope.launch {

           var response=repository.createUser(signupDTO).awaitResponse()

                controller.value=response

        }

    }



}