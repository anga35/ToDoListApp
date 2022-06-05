package com.example.todolistapp

import android.app.TimePickerDialog
import com.example.todolistapp.model.User
import com.example.todolistapp.retrofit.dto.TaskDoneDTO
import com.example.todolistapp.retrofit.dto.UserDTO

object Constants {
    val SHARED_PREFERENCE_NAME="TODO_SHARED_PREFERENCE"
    val SHARED_PREF_TOKEN="SHARED_PREFERENCE_TOKEN_SESSION"
    val SHARED_PREF_NO_CACHE="SHARED_PREFERENCE_NO_CACHE"
    val SHARED_PREF_USER_DATA="SHARED_PREFERENCE_USER_DATA"



    val RQ_READ_WRITE_PERMISSION=1
    var taskDoneList=ArrayList<Int>()
    var userData: User?=null


    var selectedDate=0L
    var selectedTime=0L
}

