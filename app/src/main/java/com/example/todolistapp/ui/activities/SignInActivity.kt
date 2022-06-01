package com.example.todolistapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.todolistapp.Constants
import com.example.todolistapp.R
import com.example.todolistapp.model.User
import com.example.todolistapp.retrofit.DTOMapper
import com.example.todolistapp.retrofit.dto.LoginDTO
import com.example.todolistapp.ui.SignInViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefEditor: SharedPreferences.Editor
    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var dtoMapper: DTOMapper
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)





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
                var userDto = response.body()
                userDto!!.profilePicture = "http://10.0.2.2:8000"+userDto.profilePicture


                var user=dtoMapper.UserDTOMapper().mapToDomain(userDto)

                val picture=userDto!!.profilePicture
                try{
                    downloadUserData(picture,user)
                }
                catch (e:Exception){
                    Toast.makeText(this@SignInActivity,
                        "Something went wrong,check your connection and try again",Toast.LENGTH_SHORT).show()
                    ll_signIn_loading.visibility= View.GONE
                }




            } else {
                Log.d("Failed", "User data failed")
                ll_signIn_loading.visibility= View.GONE
            }


        })


        btn_sign_in.setOnClickListener {
            var email = et_sign_in_email.text.toString()
            var password = et_sign_in_password.text.toString()
            if (validateLoginDetails(email, password)) {
                ll_signIn_loading.visibility= View.VISIBLE
                viewModel.loginUser(LoginDTO(email, password))
            }


        }

        tv_sign_up_intent.setOnClickListener {

            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()

        }


    }
    fun downloadUserData(pictureUrl:String,user:User){
        Glide.with(this)
            .asBitmap()
            .apply(RequestOptions().override(100,100))
            .load(pictureUrl)
            .into(object: CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val byte=ByteArrayOutputStream()
                    resource.compress(Bitmap.CompressFormat.PNG,90,byte)
                    var files=ContextCompat.getExternalFilesDirs(this@SignInActivity,null)
                    var baseDir = files[0].path
                    var profilePicDir=File(baseDir+"/Profile_picture")

                    if(!profilePicDir.exists()){
                        profilePicDir.mkdir()
                    }
                    var saveDir=File(profilePicDir.path+"/picture.jpg")

                    var fo=FileOutputStream(saveDir)
                    fo.write(byte.toByteArray())
                    fo.close()

                    user.profilePicture=saveDir.path
                    sharedPrefEditor.apply {
                        putString(Constants.SHARED_PREF_USER_DATA,Gson().toJson(user))
                        putString(Constants.SHARED_PREF_TOKEN, token)
                        apply()
                    }
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()

                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })

    }


    fun validateLoginDetails(email: String, password: String): Boolean {


        if (email.isEmpty() || password.isEmpty()) {
            return false
        }

        return true
    }


}