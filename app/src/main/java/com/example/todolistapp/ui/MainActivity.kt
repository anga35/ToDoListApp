package com.example.todolistapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistapp.Constants
import com.example.todolistapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        var isNoCache=sharedPreferences.getBoolean(Constants.SHARED_PREF_NO_CACHE, true)

        if(isNoCache){

            var intent=Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
            return

        }

        val token=sharedPreferences.getString(Constants.SHARED_PREF_TOKEN, "")

        if(token.isNullOrEmpty()){
            startActivity(Intent(this, SignInActivity::class.java))
        }







    }
}