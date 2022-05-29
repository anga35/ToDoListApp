package com.example.todolistapp.utils

interface EntityMapper<EntityModel,DomainModel> {
    fun mapToDomain(entity:EntityModel):DomainModel



}