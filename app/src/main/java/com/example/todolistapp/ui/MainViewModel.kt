package com.example.todolistapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.Constants
import com.example.todolistapp.model.User
import com.example.todolistapp.repository.MainRepository
import com.example.todolistapp.retrofit.dto.TaskDTO
import com.example.todolistapp.retrofit.dto.UserDTO
import com.example.todolistapp.utils.DataState
import com.example.todolistapp.utils.UserDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MainRepository,

    val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val userDataState: MutableLiveData<UserDataState<User>> = MutableLiveData()
    val postTaskState:MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val addTaskState:MutableLiveData<DataState<UserDTO>> = MutableLiveData()
    fun loadUserData() {
        viewModelScope.launch {


            repository.loginUserDataCache().collect { dataState ->
                userDataState.value = dataState
            }


        }


    }

    suspend fun postProfilePic(token: String, file: File):Response<Any>{
        var response:Response<Any>?=null

            val requestFile: RequestBody =

                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

            val part=MultipartBody.Part.createFormData("profile_pic", file.name, requestFile)
             response=repository.postProfilePicture(token, part).awaitResponse()

        return response!!

    }

    fun postTaskDone(token: String, taskDoneList: List<Int>) {

        val authToken= "Token $token"
        viewModelScope.launch {
            try {
                repository.postTaskDone(authToken, taskDoneList).collect {

                    postTaskState.value = it
                }
            }catch (e: Exception){
                Log.d("TASK_DONE_ERROR", e.stackTraceToString())
                postTaskState.value=DataState.Error()
            }
        }


    }


    fun postNewTask(token: String, taskList: List<TaskDTO>){

        val authToken="Token $token"
        viewModelScope.launch {

            try{
                repository.postNewTask(authToken, taskList).collect {
                    addTaskState.value=it
                }
            }catch (e: Exception){
                Log.d("TASK_ADD_ERROR", e.stackTraceToString())
                postTaskState.value=DataState.Error()

            }


        }



    }


    fun getUserToken():String{

        repository.sharedPreferences.apply {

             return getString(Constants.SHARED_PREF_TOKEN, "")!!

        }
    }


}