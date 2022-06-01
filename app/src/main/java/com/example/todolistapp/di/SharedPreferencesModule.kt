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
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
object SharedPreferencesModule {

    @ActivityScoped
    @Provides
    fun provideSharedPreference(@ActivityContext context: Context):SharedPreferences{

        val activity=context as Activity

        return activity.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE)



    }


    @ActivityScoped
    @Provides
    fun providesEditor(sharedPreferences: SharedPreferences):SharedPreferences.Editor{
       return( sharedPreferences.edit())


    }



}