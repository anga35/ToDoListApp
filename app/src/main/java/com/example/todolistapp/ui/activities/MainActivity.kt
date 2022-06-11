package com.example.todolistapp.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.DialogCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.todolistapp.Constants
import com.example.todolistapp.R
import com.example.todolistapp.adapters.TaskAddRecyclerAdapter
import com.example.todolistapp.adapters.TaskDeleteOnClickListener
import com.example.todolistapp.adapters.TaskOnClickListener
import com.example.todolistapp.adapters.TaskRecyclerAdapter
import com.example.todolistapp.model.Task
import com.example.todolistapp.model.User
import com.example.todolistapp.retrofit.DTOMapper
import com.example.todolistapp.retrofit.dto.TaskDTO
import com.example.todolistapp.ui.MainViewModel
import com.example.todolistapp.utils.DataState
import com.example.todolistapp.utils.UserDataState
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_create_task.*
import kotlinx.coroutines.*
import okhttp3.internal.wait
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import android.util.Base64
import androidx.core.net.toFile
import com.example.todolistapp.retrofit.dto.FileDTO
import java.io.FileOutputStream
import java.net.URI

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var sdfDateTime: SimpleDateFormat


    val mainViewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var file = ContextCompat.getExternalFilesDirs(this, null)


        btn_save.setOnClickListener {
            val token = mainViewModel.getUserToken()
            mainViewModel.postTaskDone(token, Constants.taskDoneList)

        }
        imb_add_task.setOnClickListener {
            val myCalender = Calendar.getInstance()
            val year = myCalender.get(Calendar.YEAR)
            val month = myCalender.get(Calendar.MONTH)
            val day = myCalender.get(Calendar.DAY_OF_MONTH)
            val hourOfDay = myCalender.get(Calendar.HOUR_OF_DAY)
            val minuteOfDay = myCalender.get(Calendar.MINUTE)
            val selectedDayTime: Long? = null
            val dialog = Dialog(this).apply {
//                var lastId=Constants.userData!!.tasks[Constants.userData!!.tasks.lastIndex].id
                val taskList: ArrayList<TaskDTO> = ArrayList<TaskDTO>()
                setContentView(R.layout.dialog_create_task)
                imb_calendar.setOnClickListener {
                    val dpd = DatePickerDialog(
                        this@MainActivity,
                        { _, selectedYear, selectedMonth, selectedDay ->
                            var selectedDate = "$selectedYear-$selectedMonth-$selectedDay"
                            tv_selected_date.text = selectedDate
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                            val date = sdf.parse(selectedDate)
                            Constants.selectedDate = date.time


                            tv_selected_date.text = correctMonthSdf(selectedDate)
                        }, year, month, day
                    ).show()
                }
                imb_time.setOnClickListener {
                    CustomPicker(hourOfDay, minuteOfDay, this).show()
                }
                imb_add_to_tasks.setOnClickListener {
                    val inputtedTaskName = et_task_name.text.toString()
                    if (Constants.selectedDate != 0L && Constants.selectedTime != 0L && !inputtedTaskName.isNullOrEmpty()) {
                        var timeFormat =
                            sdfDateTime.format(Date(Constants.selectedDate + Constants.selectedTime))

                        val timeFormatChar = timeFormat.toCharArray()
                        val index = timeFormatChar[6].toInt() + 1
                        timeFormatChar[6] = index.toChar()
                        timeFormat = String(timeFormatChar)
                        taskList.add(TaskDTO(null, inputtedTaskName, null, timeFormat, false))
                        emptyDialogInput(this)
                        val addTaskAdapter =
                            TaskAddRecyclerAdapter(this@MainActivity, taskList, sdfDateTime)
                        addTaskAdapter.taskDeleteOnClickListener =
                            object : TaskDeleteOnClickListener {
                                override fun onClick(task: TaskDTO) {
                                    addTaskAdapter.tasks.remove(task)
                                    addTaskAdapter.notifyDataSetChanged()
                                }
                            }
                        rv_add_task.layoutManager = LinearLayoutManager(this@MainActivity)
                        rv_add_task.adapter = addTaskAdapter



                        Log.d("ADD_TASK", timeFormat)
                        Log.d(
                            "VALUE_TASK",
                            (Constants.selectedDate + Constants.selectedTime).toString()
                        )


                    }
                }


                btn_done_dialog.setOnClickListener {
                    if (taskList.isEmpty()) {
                        dismiss()
                    } else {
                        mainViewModel.postNewTask(mainViewModel.getUserToken(), taskList)
                    }
                }

                btn_cancel_dialog.setOnClickListener {

                }




                show()
            }


        }


        btn_logout.setOnClickListener {
            //Clear all user cached data
            sharedPreferences.edit().apply {
                Constants.deleteUserProfileImage(this@MainActivity,"Profile_picture")
                Constants.deleteUserProfileImage(this@MainActivity,"temp_profile")
                putString(Constants.SHARED_PREF_TOKEN,"")
                putString(Constants.SHARED_PREF_USER_DATA,"")
                apply()
                startActivity(Intent(this@MainActivity,SignInActivity::class.java))
                finishAffinity()


            }


        }

        mainViewModel.userDataState.observe(this, Observer { dataState ->

            //Handling various user data states
            when (dataState) {
                is UserDataState.UserData -> {

                    val user = dataState.data
                    Constants.userData = user
                    tv_user_email.text = user.fullname
                    if(user.profilePicture.isNotEmpty()) {
                        val file = File(user.profilePicture)
                        loadProfilePic(file)
                    }

                    //Filter tasks to only undone tasks
                    val filteredTask = user.tasks.filter { task -> !task.isDone }
                    rv_tasks.layoutManager = LinearLayoutManager(this)
                    val adapter = TaskRecyclerAdapter(this, filteredTask, sdfDateTime)
                    adapter.taskOnClickListener = object : TaskOnClickListener {
                        override fun onClick(task: Task) {
                            task.isDone = !task.isDone
                            if (task.isDone) {
                                Constants.taskDoneList.add(task.id)
                            } else {
                                Constants.taskDoneList.remove(task.id)
                            }

                            adapter.notifyDataSetChanged()
                        }

                    }
                    rv_tasks.adapter = adapter
                    if (!Constants.isTimeLooper) {
                        Constants.isTimeLooper = true

                        lifecycleScope.launch {
                            while (true) {
                                delay(1000)
                                withContext(Dispatchers.Main) {
                                    adapter.notifyDataSetChanged()
                                }

                            }
                        }

                    }

                }
                is UserDataState.NewUser -> {
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                }
                is UserDataState.AnonymousUser -> {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }

            }


        })

        mainViewModel.postTaskState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    btn_save.visibility = View.INVISIBLE
                    doneLoading()
                    for (task in Constants.userData!!.tasks) {
                        if (task.id in Constants.taskDoneList) {
                            task.isDone = true
                        }
                    }
                    Constants.taskDoneList.clear()
                    storeUserDataToDevice(Constants.userData!!)
                    mainViewModel.loadUserData()

                }
                is DataState.Loading -> {
                    showLoading()
                }
                is DataState.Error -> {
                    doneLoading()

                }

            }

        })

        mainViewModel.addTaskState.observe(this, Observer { dataState ->

            if (dataState is DataState.Loading) {
                showLoading()
            } else if (dataState is DataState.Error) {
                Toast.makeText(
                    this,
                    "Could not add new tasks,check your connection and try again",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (dataState is DataState.Success) {
                val userDto = dataState.data
                val user = DTOMapper().UserDTOMapper().mapToDomain(userDto)
                user.profilePicture = Constants.userData!!.profilePicture
                Constants.userData = user
                storeUserDataToDevice(user)
                mainViewModel.loadUserData()
                doneLoading()


            }


        })

        mainViewModel.loadUserData()


        imv_profile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, Constants.PROFILE_PIC_RESULT_CODE)
        }

    }

    private fun showLoading() {
        ll_loading.visibility = View.VISIBLE
    }

    private fun doneLoading() {
        ll_loading.visibility = View.GONE
    }


    private fun storeUserDataToDevice(user: User) {
        var userData = Gson().toJson(user)


        sharedPreferences.edit().apply {
            putString(Constants.SHARED_PREF_USER_DATA, userData)
            apply()

        }


    }

    fun emptyDialogInput(dialog: Dialog) {


        dialog.et_task_name.text.clear()
        dialog.tv_selected_date.text = "Select date"
        dialog.tv_selected_time.text = "Select time"
    }


    inner class CustomPicker(val hourOfDay: Int, minuteOfDay: Int, val dialog: Dialog) :
        TimePickerDialog(
            this,
            { timePicker, selectedHour, selectedMinute ->
                Log.d("SELECTED_HOUR", selectedHour.toString())
                Log.d("SELECTED_MINUTE", selectedMinute.toString())
                val totalTime = (selectedHour * 60L) + (selectedMinute)
                val totalDate = (totalTime * 60000)
                Constants.selectedTime = totalDate


                dialog.tv_selected_time.text = "$selectedHour:$selectedMinute"

            },
            hourOfDay,
            minuteOfDay,
            true
        ) {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onTimeChanged(view: TimePicker?, hour: Int, minute: Int) {
            if (hour < hourOfDay) {
                dialog.tv_selected_date.text = "Select time"
                this.dismiss()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Constants.PROFILE_PIC_RESULT_CODE) {
                if (data!!.data != null) {
                    val inputStream=contentResolver.openInputStream(data.data!!)






                    val fileUri = data.data
                    val bitmap = convertUriToBitmap(data.data!!)
                    val fileTemp=Constants.storeImageToDevice(bitmap,this,"temp_profile")


val job=lifecycleScope.launch (Dispatchers.IO){

    withContext(Dispatchers.Main){
        showLoading()
    }

    val response=mainViewModel.postProfilePic(mainViewModel.getUserToken(),
        fileTemp)
    withContext(Dispatchers.Main){
       doneLoading()
    }
    if(response.isSuccessful){
        val file = Constants.storeImageToDevice(bitmap, this@MainActivity,"Profile_picture")

        withContext(Dispatchers.Main){
            loadProfilePic(fileTemp)
        }

    }
    else{
        withContext(Dispatchers.Main){
            Toast.makeText(this@MainActivity,"Make sure you have internet access and try again",Toast.LENGTH_SHORT).show()
        }

    }

}

                    }
            }

        }
    }


    fun correctMonthSdf(date:String):String{

        val charDate=date.toCharArray()
        charDate[5]=(charDate[5].toInt()+1).toChar()
        return String(charDate)


    }

    fun loadProfilePic(file: File) {
        val bitmap = BitmapFactory.decodeFile(file.path)
        imv_profile.setImageBitmap(bitmap)

    }


    fun convertUriToBitmap(uri: Uri): Bitmap {

        val inputStream = contentResolver.openInputStream(uri)
        val drawable = Drawable.createFromStream(inputStream, uri.toString())
        val bitmap = getBitmapFromDrawable(drawable)
        return bitmap


    }

    fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap

    }


}


//    fun deriveRemainingTime(date:Long):String{
//
//        val dateInMinutes=date/60000
//        val currentDateInMinutes=System.currentTimeMillis()/60000
//        var diffInHours=0
//        var passed=false
//        if(currentDateInMinutes>dateInMinutes){
//            passed=true
//        }
//
//        val differenceInMin=dateInMinutes-currentDateInMinutes
//        if(differenceInMin<60){
//            return "$differenceInMin Minutes remaining"
//        }
//        else if(differenceInMin>60){
//            diffInHours=differenceInMin/
//        }
//
//    }


