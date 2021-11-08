package com.example.todo_app

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.model.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class Task_Adapter(var data: MutableList<Task>) : RecyclerView.Adapter<TaskHolder>() {
    val db = Firebase.firestore


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_task, parent, false)



        return TaskHolder(v)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.taskTitleTextView.text = data[position].title
        holder.taskNoteTextView.text = data[position].descrption
        holder.taskDueDateTextView.text = "Due: " + data[position].dueDate.toString()

        if (data[position].stauts) {
            holder.checkState.isChecked=true
            holder.taskTitleTextView.setPaintFlags(
                holder.taskTitleTextView.getPaintFlags() or
                        android.graphics.Paint.STRIKE_THRU_TEXT_FLAG)
            holder.taskNoteTextView.setPaintFlags(
                holder.taskNoteTextView.getPaintFlags() or
                        android.graphics.Paint.STRIKE_THRU_TEXT_FLAG)


        } else {

            holder.checkState.isChecked=false
        }

        holder.checkState.setOnClickListener {
                db.collection("Tasks")
                    .document(data[position].id!!)
                    .update("compeleted", true)
                holder.taskTitleTextView.setPaintFlags(
                    holder.taskTitleTextView.getPaintFlags() or
                            android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                )

                holder.taskNoteTextView.setPaintFlags(
                    holder.taskNoteTextView.getPaintFlags() or
                            android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                )


        }






        holder.deleteCardView.setOnClickListener {
            var deleteDialog = AlertDialog.Builder(holder.deleteCardView.context)
                .setTitle("Delete")
                .setMessage("Do you want to delete?")
                .setPositiveButton("Yes") { dialog, which ->
                    db.collection("Tasks").document(data[position].id!!)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                holder.deleteCardView.context,
                                "Deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                holder.deleteCardView.context,
                                "${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .setIcon(R.drawable.ic_baseline_delete_24)
                .show()
        }


        holder.updateImage.setOnClickListener {
            var u= LayoutInflater.from(holder.updateImage.context).inflate(R.layout.update_dialog, null)

            var updateDialog = AlertDialog.Builder(holder.updateImage.context)
                .setView(u)
                .show()
            var dateImageView = u.findViewById<ImageView>(R.id.imageViewDate)

            dateImageView.setOnClickListener {
                var c = Calendar.getInstance()
                var year = c.get(Calendar.YEAR)
                var month = c.get(Calendar.MONTH)
                var day = c.get(Calendar.DAY_OF_MONTH)

                var DatePickerDialog = DatePickerDialog(
                    holder.updateImage.context,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                        holder.taskDueDateTextView.text = "$dayOfMonth/${month + 1}/$year"
                    },
                    year,
                    month,
                    day
                )
                DatePickerDialog.show()
            }
            var updateDialogBtn = u.findViewById<Button>(R.id.buttonUpdate)
            var TasktitleEditText = u.findViewById<EditText>(R.id.EditTextTaskTitle)
            var TaskdescrptionEditText = u.findViewById<EditText>(R.id.EditTextTaskNote)

            updateDialogBtn.setOnClickListener {
                db.collection("Tasks")
                    .document(data[position].id!!)
                    .update(mapOf("title" to TasktitleEditText.text,
                        "descrption" to TaskdescrptionEditText.text,
                        "dueDate" to holder.taskDueDateTextView,
                        "compeleted" to data[position].stauts)
                    )
                    .addOnSuccessListener {
                        Toast.makeText(
                            holder.updateImage.context,
                            "Task has been updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    .addOnFailureListener {e->
                        Log.w(ContentValues.TAG, "Error updating document", e)
                    }
                updateDialog.dismiss()

            }

        }

            }





    override fun getItemCount(): Int {
        return data.size
    }


}


class TaskHolder(v: View) : RecyclerView.ViewHolder(v) {
    var taskTitleTextView = v.findViewById<TextView>(R.id.textViewTaskTitle)
    var taskNoteTextView = v.findViewById<TextView>(R.id.textViewTaskNote)
    var taskDueDateTextView = v.findViewById<TextView>(R.id.textViewDueDate)
    var createDateTextView = v.findViewById<TextView>(R.id.textViewCreationDate)
    var updateImage = v.findViewById<ImageView>(R.id.imageViewUpdate)
    var deleteCardView = v.findViewById<CardView>(R.id.deleteCardview)
    var checkState = v.findViewById<CheckBox>(R.id.checkBoxState)


}