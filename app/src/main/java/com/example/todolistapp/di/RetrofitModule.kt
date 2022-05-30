package com.example.todolistapp.di

import com.example.todolistapp.retrofit.TodoRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {





    @Singleton
    @Provides
    fun provideRetrofitBuild(): Retrofit {
        return Retrofit.Builder().baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }

    @Singleton
    @Provides
    fun provideTodoService(retrofit: Retrofit):TodoRetrofit{

        return retrofit.create(TodoRetrofit::class.java)

    }


}