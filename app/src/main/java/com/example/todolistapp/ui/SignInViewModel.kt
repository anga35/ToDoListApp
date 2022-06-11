package com.example.todolistapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.Token
import com.example.todolistapp.repository.MainRepository
import com.example.todolistapp.retrofit.dto.LoginDTO
import com.example.todolistapp.retrofit.dto.UserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.awaitResponse
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class SignInViewModel
    @Inject
    constructor(val repository: MainRepository,val savedStateHandle: SavedStateHandle) :ViewModel(){

        var tokenLiveData:MutableLiveData<Response<Token>?> = MutableLiveData()
        val userLiveData:MutableLiveData<Response<UserDTO>> = MutableLiveData()

    fun loginUser(loginDTO: LoginDTO){

        viewModelScope.launch{
var response:Response<Token>? =null
            try{
                response=repository.loginUser(loginDTO).awaitResponse()

            }
            catch (e:Exception){
response=null

                Log.d("TIMEOUT","Connection Timeout")
            }
            tokenLiveData.value=response


        }


    }

    fun getUserData(token:String){


        viewModelScope.launch {
            val response=repository.todoRetrofit.getUser("Token $token").awaitResponse()
            userLiveData.value=response

        }



    }





}