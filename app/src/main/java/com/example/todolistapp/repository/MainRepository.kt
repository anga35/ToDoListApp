package com.example.todolistapp.repository

import com.example.todolistapp.retrofit.DTOMapper
import com.example.todolistapp.retrofit.TodoRetrofit
import com.example.todolistapp.retrofit.dto.SignupDTO
import kotlinx.coroutines.*
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





}