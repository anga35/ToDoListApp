package com.example.todolistapp.ui

import android.app.Application
import com.example.todolistapp.ui.activities.MainActivity
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {

   lateinit var mainActivity:MainActivity


}