package com.example.todolistapp.retrofit.dto

import com.google.gson.annotations.SerializedName

class SignupDTO(
    @SerializedName(value = "email")
    val email:String,
    val fullname:String,
    val password:String,
    val password1:String,

) {


}