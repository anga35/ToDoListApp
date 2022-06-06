package com.example.todolistapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.Constants
import com.example.todolistapp.R
import com.example.todolistapp.model.Task
import com.example.todolistapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_task.view.*
import java.text.SimpleDateFormat

class TaskRecyclerAdapter(val context: Context,var tasks:List<Task>,val sdf:SimpleDateFormat):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var taskOnClickListener: TaskOnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task,parent,false))
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TaskViewHolder){
            val mainActivity=context as MainActivity
            val task=tasks[position]
            var taskName=task.name
            val timeInMillis=sdf.parse(task.deadline).time
            taskName=taskName+" "+Constants.deriveRemainingTime(timeInMillis,sdf)


                holder.itemView.tv_task_name.text=taskName

            if(!task.isDone){


                holder.itemView.imv_task_check.setImageResource(R.drawable.unchecked_task)

                if(Constants.taskDoneList.isEmpty()){
                    mainActivity.btn_save.visibility=View.INVISIBLE
                }
            }
            else{

                holder.itemView.imv_task_check.setImageResource(R.drawable.checked_task)
                if(mainActivity.btn_save.visibility==View.INVISIBLE){
                    mainActivity.btn_save.visibility=View.VISIBLE



                }
            }
            holder.itemView.imv_task_check.setOnClickListener {
                taskOnClickListener.onClick(task)
            }
        }
    }

    override fun getItemCount(): Int {
       return tasks.size
    }

    class TaskViewHolder(view: View):RecyclerView.ViewHolder(view)



}