package com.example.todolistapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.todolistapp.Constants
import com.example.todolistapp.R
import com.example.todolistapp.retrofit.dto.LoginDTO
import com.example.todolistapp.ui.SignInViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefEditor: SharedPreferences.Editor
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        var token = ""




        viewModel.tokenLiveData.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Log.d("Success", response.body()!!.toString())
                token = response.body()!!.token
                viewModel.getUserData(response.body()!!.token)
            } else {
                Log.d("Failed", response.body()!!.toString())

            }


        })

        viewModel.userLiveData.observe(this, Observer { response ->

            if (response.isSuccessful) {
                Log.d("Success", "User data received")
                Constants.userDto = response.body()
                Constants.userDto!!.profilePicture = "http://10.0.2.2:8000"+Constants.userDto!!.profilePicture
                sharedPrefEditor.apply {
                    putString(Constants.SHARED_PREF_USER_DATA,Gson().toJson(Constants.userDto))
                    putString(Constants.SHARED_PREF_TOKEN, token)
                    apply()



                }






                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.d("Failed", "User data failed")

            }


        })


        btn_sign_in.setOnClickListener {
            var email = et_sign_in_email.text.toString()
            var password = et_sign_in_password.text.toString()
            if (validateLoginDetails(email, password)) {
                viewModel.loginUser(LoginDTO(email, password))
            }


        }

        tv_sign_up_intent.setOnClickListener {

            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()

        }


    }

    fun validateLoginDetails(email: String, password: String): Boolean {


        if (email.isEmpty() || password.isEmpty()) {
            return false
        }

        return true
    }


}