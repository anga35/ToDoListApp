package com.example.todolistapp.repository

import com.example.todolistapp.model.Token
import com.example.todolistapp.retrofit.DTOMapper
import com.example.todolistapp.retrofit.TodoRetrofit
import com.example.todolistapp.retrofit.dto.LoginDTO
import com.example.todolistapp.retrofit.dto.SignupDTO
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call

class MainRepository
constructor(
    dtoMapper: DTOMapper,
    val todoRetrofit: TodoRetrofit
)
{

   fun createUser(signupDTO: SignupDTO):Call<Any> {

return todoRetrofit.createUser(signupDTO)

    }


    fun loginUser(loginDTO: LoginDTO):Call<Token>{

        return todoRetrofit.loginUser(loginDTO)

    }




}