package com.example.todolistapp.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolistapp.Constants
import com.example.todolistapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_welcome.*
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btn_get_started.setOnClickListener {

            sharedPrefEditor.putBoolean(Constants.SHARED_PREF_NO_CACHE,false)
            sharedPrefEditor.apply()
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }
    }
}