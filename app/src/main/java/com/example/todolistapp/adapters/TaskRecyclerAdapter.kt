package com.example.todolistapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.model.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskRecyclerAdapter(val context: Context,val tasks:List<Task>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var taskOnClickListener: TaskOnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task,parent,false))
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TaskViewHolder){
            val task=tasks[position]
            val taskName=task.name
                holder.itemView.tv_task_name.text=taskName

            if(!task.isDone){
                holder.itemView.imv_task_check.setImageResource(R.drawable.unchecked_task)
            }
            else{
                holder.itemView.imv_task_check.setImageResource(R.drawable.checked_task)
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