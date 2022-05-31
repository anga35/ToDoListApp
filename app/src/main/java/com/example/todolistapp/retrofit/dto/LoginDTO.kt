package com.example.todolistapp.retrofit.dto

import com.google.gson.annotations.SerializedName

class LoginDTO(
    @SerializedName(value = "email")
    val email:String,val password:String) {
}