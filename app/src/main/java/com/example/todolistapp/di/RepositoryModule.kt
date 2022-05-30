package com.example.todolistapp.di

import com.example.todolistapp.repository.MainRepository
import com.example.todolistapp.retrofit.DTOMapper
import com.example.todolistapp.retrofit.TodoRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(dtoMapper: DTOMapper,todoRetrofit: TodoRetrofit):MainRepository{

        return MainRepository(dtoMapper, todoRetrofit)

    }


}