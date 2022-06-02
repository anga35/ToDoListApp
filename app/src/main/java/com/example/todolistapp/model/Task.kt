package com.example.todolistapp.model

data class Task(val id:Int, val name:String, val createDate:String,
                val deadline:String?, var isDone:Boolean) {
}