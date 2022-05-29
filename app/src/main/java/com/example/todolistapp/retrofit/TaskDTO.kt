package com.example.todolistapp.retrofit

import com.google.gson.annotations.SerializedName

class TaskDTO(val id:Int,val name:String,
              @SerializedName(value = "create_data")
              val createDate:String,

              val deadline:String?,

              @SerializedName(value = "is_done")
              val isDone:Boolean) {
}