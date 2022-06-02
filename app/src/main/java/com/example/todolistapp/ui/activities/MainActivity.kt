package com.example.todolistapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.todolistapp.Constants
import com.example.todolistapp.R
import com.example.todolistapp.adapters.TaskOnClickListener
import com.example.todolistapp.adapters.TaskRecyclerAdapter
import com.example.todolistapp.model.Task
import com.example.todolistapp.model.User
import com.example.todolistapp.ui.MainViewModel
import com.example.todolistapp.utils.UserDataState
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.wait
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val mainViewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var file=ContextCompat.getExternalFilesDirs(this, null)


        mainViewModel.userDataState.observe(this, Observer {
            dataState->
            when(dataState){
                is UserDataState.UserData->{
                    val user=dataState.data
                    tv_user_email.text=user.fullname

                    Glide.with(this)
                        .asBitmap()
                        .load(user.profilePicture)
                        .into(object: CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                imv_profile.setImageBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                TODO("Not yet implemented")
                            }

                        })

                        val filteredTask=user.tasks.filter { task -> !task.isDone  }
                        rv_tasks.layoutManager=LinearLayoutManager(this)
                        val adapter=TaskRecyclerAdapter(this,filteredTask)
                        adapter.taskOnClickListener=object:TaskOnClickListener{
                            override fun onClick(task: Task) {
                                task.isDone= !task.isDone
                                adapter.notifyDataSetChanged()
                            }

                        }
                        rv_tasks.adapter=adapter
                    
                }
                is UserDataState.NewUser->{
                    startActivity(Intent(this,WelcomeActivity::class.java))
                    finish()
                }
                is UserDataState.AnonymousUser->{
                    startActivity(Intent(this,SignInActivity::class.java))
                    finish()
                }

            }



        })


        mainViewModel.loadUserData()

    }



}