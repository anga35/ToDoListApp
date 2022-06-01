package com.example.todolistapp.model

data class User (val email:String, val fullname:String, var profilePicture:String, val tasks:List<Task>)
