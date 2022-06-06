package com.example.todolistapp.retrofit

import com.example.todolistapp.model.Task
import com.example.todolistapp.model.Token
import com.example.todolistapp.retrofit.dto.LoginDTO
import com.example.todolistapp.retrofit.dto.SignupDTO
import com.example.todolistapp.retrofit.dto.TaskDTO
import com.example.todolistapp.retrofit.dto.UserDTO
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface TodoRetrofit {


    @POST("user/create/")
    fun createUser(@Body signUpDTO:SignupDTO):Call<Any>

    @Headers("Accept: application/json")
    @POST("user/login/")
    fun loginUser(@Body loginDTO: LoginDTO):Call<Token>


    @GET("user/get-user/")
    fun getUser(@Header("AUTHORIZATION") tokenVal:String):Call<UserDTO>

    @FormUrlEncoded
    @POST("task/task-done/")
    fun taskDone(@Header("AUTHORIZATION") tokenVal:String,@Field("pk") doneList: List<Int>):Call<Any>

    @POST("task/create/")
    fun taskCreate(@Header("AUTHORIZATION") tokenVal: String,@Body taskList:List<TaskDTO>):Call<UserDTO>



}