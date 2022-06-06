package com.example.todolistapp.repository

import android.content.Intent
import android.content.SharedPreferences
import android.provider.ContactsContract
import android.util.Log
import com.example.todolistapp.Constants
import com.example.todolistapp.model.Token
import com.example.todolistapp.model.User
import com.example.todolistapp.retrofit.DTOMapper
import com.example.todolistapp.retrofit.TodoRetrofit
import com.example.todolistapp.retrofit.dto.LoginDTO
import com.example.todolistapp.retrofit.dto.SignupDTO
import com.example.todolistapp.retrofit.dto.TaskDTO
import com.example.todolistapp.retrofit.dto.UserDTO
import com.example.todolistapp.utils.DataState
import com.example.todolistapp.utils.UserDataState
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.awaitResponse

class MainRepository
constructor(
    dtoMapper: DTOMapper,
    val todoRetrofit: TodoRetrofit,
    val sharedPreferences: SharedPreferences
) {




    fun createUser(signupDTO: SignupDTO): Call<Any> {

        return todoRetrofit.createUser(signupDTO)

    }


    fun loginUser(loginDTO: LoginDTO): Call<Token> {

        return todoRetrofit.loginUser(loginDTO)

    }

    //Return flow state for fetching user
    fun loginUserDataCache() = flow<UserDataState<User>> {
        emit(UserDataState.Loading())
        var isNoCache = sharedPreferences.getBoolean(Constants.SHARED_PREF_NO_CACHE, true)

        if (isNoCache) {
            emit(UserDataState.NewUser())
            return@flow
        }
        val token = sharedPreferences.getString(Constants.SHARED_PREF_TOKEN, "")
        if (token.isNullOrEmpty()) {
            emit(UserDataState.AnonymousUser())
        } else {
            val userData = sharedPreferences.getString(Constants.SHARED_PREF_USER_DATA, "")


            val user = Gson().fromJson(userData, User::class.java)
            emit(UserDataState.UserData(user))
        }

    }





    fun postTaskDone(token:String,pkList:List<Int>)= flow<DataState<Boolean>> {
        emit(DataState.Loading())


            val response=todoRetrofit.taskDone(token,pkList).awaitResponse()
            if(response.isSuccessful) {
                emit(DataState.Success(true))
            }else{
                emit(DataState.Error())
            }







    }


    fun postNewTask(token: String,taskList:List<TaskDTO>)=flow<DataState<UserDTO>>{

        emit(DataState.Loading())
        val response=todoRetrofit.taskCreate(token,taskList).awaitResponse()
        if(response.isSuccessful){
            emit(DataState.Success(response.body()!!))
        }
        else{
            emit(DataState.Error())
        }


    }





}