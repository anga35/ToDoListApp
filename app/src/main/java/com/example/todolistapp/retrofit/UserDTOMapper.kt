package com.example.todolistapp.retrofit

import com.example.todolistapp.model.User
import com.example.todolistapp.utils.EntityMapper

class UserDTOMapper : EntityMapper<UserDTO, User> {
    override fun mapToDomain(entity: UserDTO): User {
        return User(
            email = entity.email,
            fullname = entity.fullname,
            profilePicture = entity.profilePicture,
            tasks = entity.tasks

        )


    }
}