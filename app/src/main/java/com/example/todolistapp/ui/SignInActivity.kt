package com.example.todolistapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.todolistapp.R
import com.example.todolistapp.RegisterationViewModel
import com.example.todolistapp.retrofit.dto.LoginDTO
import com.example.todolistapp.utils.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sign_in.*

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {


 private val viewModel:SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        viewModel.responseLiveData.observe(this, Observer {
            response->
            if(response.isSuccessful){
                Log.d("Success",response.body()!!.toString())
            }
            else{
                Log.d("Failed", response.body()!!.toString())

            }


        })


        btn_sign_in.setOnClickListener {
            var email=et_sign_in_email.text.toString()
            var password=et_sign_in_password.text.toString()
            if(validateLoginDetails(email, password)){
                viewModel.loginUser(LoginDTO(email, password))
            }



        }


    }

    fun validateLoginDetails(email:String,password:String):Boolean{



        if(email.isEmpty() || password.isEmpty()){
            return false
        }

        return true
    }


}