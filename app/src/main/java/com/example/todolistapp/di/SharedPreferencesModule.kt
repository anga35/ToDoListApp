package com.example.todolistapp.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.todolistapp.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context):SharedPreferences{



        return context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)



    }


    @Singleton
    @Provides
    fun providesEditor(sharedPreferences: SharedPreferences):SharedPreferences.Editor{
       return( sharedPreferences.edit())


    }



}