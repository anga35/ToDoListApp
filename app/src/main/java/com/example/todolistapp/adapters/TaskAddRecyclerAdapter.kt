package com.example.todolistapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.Constants
import com.example.todolistapp.R
import com.example.todolistapp.retrofit.dto.TaskDTO
import dagger.hilt.android.HiltAndroidApp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_add_task.view.*
import kotlinx.android.synthetic.main.item_task.view.*
import kotlinx.android.synthetic.main.item_task.view.tv_task_name
import java.text.SimpleDateFormat
import javax.inject.Inject


class TaskAddRecyclerAdapter constructor(val context:Context,val tasks:ArrayList<TaskDTO>,val sdf:SimpleDateFormat):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var taskDeleteOnClickListener: TaskDeleteOnClickListener




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_task,parent,false))
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TaskViewHolder){

            val task=tasks[position]
            val taskName=task.name


            val date=sdf.parse(task.deadline)
            holder.itemView.tv_addTask_name.text=taskName+" "+deriveRemainingTime(date.time)


            Log.d("RV_TASK",date.time.toString())




            holder.itemView.imb_delete_task.setOnClickListener {
                taskDeleteOnClickListener.onClick(task)
            }
        }
    }


    fun deriveRemainingTime(timeInMilli:Long):String{

        var remainderTime=0L
        var limit="left"
        val currentTime=System.currentTimeMillis()
        val temp = sdf.format(System.currentTimeMillis())

        if(currentTime>timeInMilli){
            remainderTime=currentTime-timeInMilli
            limit="passed deadline"
        }
        else{
            remainderTime=timeInMilli-currentTime
        }
        val timeInMin= remainderTime/60000
        if(timeInMin<60){
            if (timeInMin==1L) return "$timeInMin minute $limit" else  return "$timeInMin minutes $limit"

        }
        val timeInHr=timeInMin/60
        if(timeInHr<24){
            if(timeInHr==1L) return "$timeInHr hour $limit" else return "$timeInHr hours $limit"

        }
        val timeInDays=timeInHr/24
        if(timeInDays<7){
            if(timeInDays==1L) return "$timeInDays day $limit" else return "$timeInDays days $limit"

        }

        val timeInWeeks=timeInDays/7
        if(timeInWeeks<4){
            if(timeInWeeks==1L) return "$timeInWeeks week $limit" else return "$timeInWeeks weeks $limit"

        }

        val timeInMonths=timeInWeeks/4
        if(timeInMonths<12){
            if(timeInMonths==1L) return "$timeInMonths month $limit" else return "$timeInMonths months $limit"

        }

        val timeInYears=timeInMonths/12

        if(timeInMonths>=12){
            if(timeInYears==1L) return "$timeInYears year $limit" else return "$timeInYears years $limit"

        }

        return "invalid"
    }



    override fun getItemCount(): Int {
        return tasks.size
    }

    class TaskViewHolder(view: View): RecyclerView.ViewHolder(view)



}