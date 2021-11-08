package com.example.todo_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class Task_Adapter(var data:MutableList<Task>):RecyclerView.Adapter<TaskHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_task,parent,false)

        return TaskHolder(v)
     }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.taskTitleTextView.text=data[position].title
        holder.taskNoteTextView.text=data[position].descrption
        holder.taskDueDateTextView.text=data[position].dueDate.toString()
        //holder.createDateTextView.text=data[position].createDate.toString()
//        holder.checkRadio.setOnClickListener {
//            if(holder.checkRadio.isChecked){
//
//                holder.taskTitleTextView.setPaintFlags(holder.taskTitleTextView.getPaintFlags() or
//                        android.graphics.Paint.STRIKE_THRU_TEXT_FLAG)
//
//                holder.taskNoteTextView.setPaintFlags(holder.taskNoteTextView.getPaintFlags() or
//                        android.graphics.Paint.STRIKE_THRU_TEXT_FLAG)
//
//
//            }
//     }
        holder.updateImage.setOnClickListener {  }
        holder.deleteCardView.setOnClickListener {  }




    }

    override fun getItemCount(): Int {
        return data.size
     }


}


class TaskHolder(v: View): RecyclerView.ViewHolder(v){
    var taskTitleTextView= v.findViewById<TextView>(R.id.textViewTaskTitle)
    var taskNoteTextView= v.findViewById<TextView>(R.id.textViewTaskNote)
    var taskDueDateTextView= v.findViewById<TextView>(R.id.textViewDueDate)
    var createDateTextView= v.findViewById<TextView>(R.id.textViewCreationDate)
    //var checkRadio= v.findViewById<RadioButton>(R.id.radioButton)
    var updateImage= v.findViewById<ImageView>(R.id.imageViewUpdate)
    var deleteCardView= v.findViewById<CardView>(R.id.deleteCardview)



}