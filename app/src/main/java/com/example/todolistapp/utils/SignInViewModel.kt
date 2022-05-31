package com.example.todolistapp.utils

import android.media.session.MediaSession
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.Token
import com.example.todolistapp.repository.MainRepository
import com.example.todolistapp.retrofit.TodoRetrofit
import com.example.todolistapp.retrofit.dto.LoginDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.awaitResponse
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class SignInViewModel
    @Inject
    constructor(val repository: MainRepository,val savedStateHandle: SavedStateHandle) :ViewModel(){

        var responseLiveData:MutableLiveData<Response<Token>> = MutableLiveData()


    fun loginUser(loginDTO: LoginDTO){

        viewModelScope.launch{

            try{
                var response=repository.loginUser(loginDTO).awaitResponse()
                responseLiveData.value=response
            }
            catch (e:Exception){
                Log.d("TIMEOUT","Connection Timeout")
            }



        }


    }




}