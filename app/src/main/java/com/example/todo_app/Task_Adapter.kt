package com.example.todo_app

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.model.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Task_Adapter(var data:MutableList<Task>):RecyclerView.Adapter<TaskHolder>() {
    val db = Firebase.firestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_task,parent,false)

        return TaskHolder(v)
     }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.taskTitleTextView.text=data[position].title
        holder.taskNoteTextView.text=data[position].descrption
        holder.taskDueDateTextView.text="Due: "+data[position].dueDate.toString()

        if(data[position].stauts){
            holder.checkState.isChecked

         }else {
            println(holder.checkState.isChecked.not())
        }


        holder.checkState.setOnClickListener {


            if (holder.checkState.isChecked) {
                db.collection("Tasks")
                    .document(data[position].id!!)
                    .update("compeleted",true)
                holder.taskTitleTextView.setPaintFlags(
                    holder.taskTitleTextView.getPaintFlags() or
                            android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                )

                holder.taskNoteTextView.setPaintFlags(
                    holder.taskNoteTextView.getPaintFlags() or
                            android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                )

            }
        }


        holder.updateImage.setOnClickListener {  }
        holder.deleteCardView.setOnClickListener {
            var deleteDialog=AlertDialog.Builder(holder.deleteCardView.context)
                .setTitle("Delete")
                .setMessage("Do you want to delete?")
                .setPositiveButton("Yes"){dialog, which->
                    db.collection("Tasks").document(data[position].id!!)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(holder.deleteCardView.context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(holder.deleteCardView.context, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    dialog.dismiss()
                }
                .setNegativeButton("No"){dialog,which->
                    dialog.dismiss()
                }
                .setIcon(R.drawable.ic_baseline_delete_24)
                .show()
        }




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
    var checkState = v.findViewById<CheckBox>(R.id.checkBoxState)




}