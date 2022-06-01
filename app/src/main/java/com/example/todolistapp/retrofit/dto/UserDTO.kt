package com.example.todolistapp.retrofit.dto

import com.example.todolistapp.model.Task

data class UserDTO(val email:String,val fullname:String,
                   val profilePicture:String,val tasks: List<Task>?) {
}