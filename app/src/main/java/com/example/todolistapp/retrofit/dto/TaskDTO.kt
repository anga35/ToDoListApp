package com.example.todolistapp.retrofit.dto

import com.google.gson.annotations.SerializedName




class TaskDTO(val id:Int?,val name:String,
              @SerializedName(value = "create_date")
              val createDate:String?,

              val deadline:String?,

              @SerializedName(value = "is_done")
              val isDone:Boolean) {
}