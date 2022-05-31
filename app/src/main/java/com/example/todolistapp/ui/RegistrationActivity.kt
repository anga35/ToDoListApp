package com.example.todolistapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.example.todolistapp.R
import com.example.todolistapp.RegisterationViewModel
import com.example.todolistapp.retrofit.TodoRetrofit
import com.example.todolistapp.retrofit.dto.SignupDTO
import com.example.todolistapp.retrofit.dto.UserDTO
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.awaitResponse
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {




    private  val viewModel:RegisterationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

viewModel.controller.observe(this, Observer { response->

        if(response.isSuccessful){
            Log.d("Response Message","SUCCESS")
            Toast.makeText(this,"Login successful",Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                delay(1500)
                startActivity(Intent(this@RegistrationActivity,SignInActivity::class.java))
            }
        }
        else{
            Log.d("Response Message","Failure")
        }


})

        btn_register.setOnClickListener {
            if (validateInputFields()){
                val name = et_name.text.toString()
                val email = et_email.text.toString()
                val password = et_password.text.toString()
                val confirmPassword = et_confirm_password.text.toString()

                var signupDTO=SignupDTO(email=email,fullname = name,password=password,password1 = confirmPassword)

                viewModel.createUser(signupDTO)





            }

        }


    }

    fun validateInputFields(): Boolean {
        val name = et_name.text.toString()
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        val confirmPassword = et_confirm_password.text.toString()


        if (name.isEmpty() || email.isEmpty()
            || password.isEmpty() || confirmPassword.isEmpty()
        ){
            return false
        }

return true
    }


}