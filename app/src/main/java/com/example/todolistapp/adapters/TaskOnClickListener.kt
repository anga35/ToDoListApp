package com.example.todolistapp.adapters

import com.example.todolistapp.model.Task
import com.example.todolistapp.retrofit.dto.TaskDTO

interface TaskOnClickListener {

    fun onClick(task:Task)

}

interface TaskDeleteOnClickListener {

    fun onClick(task:TaskDTO)

}