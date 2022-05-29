package com.example.todolistapp.retrofit

import com.example.todolistapp.model.Task
import com.example.todolistapp.utils.EntityMapper

class TaskDTOMapper : EntityMapper<TaskDTO, Task> {
    override fun mapToDomain(entity: TaskDTO): Task {
        return Task(
            id = entity.id,
            name = entity.name,
            createDate = entity.createDate,
             deadline = entity.deadline,
            isDone = entity.isDone

            )


    }
}