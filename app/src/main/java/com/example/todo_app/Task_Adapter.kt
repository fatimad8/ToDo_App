package com.example.todo_app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.model.Task
import com.google.android.material.dialog.MaterialDialogs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class Task_Adapter(var data: MutableList<Task>) : RecyclerView.Adapter<TaskHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row_task, parent, false)


        return TaskHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val db = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val rootRef = FirebaseFirestore.getInstance()

         db.collection("users").document(uid).collection("task")
        //db.collection("Tasks")
            .get()




        holder.taskTitleTextView.text = data[position].title.capitalize()
        //holder.taskNoteTextView.text = data[position].descrption
        holder.taskDueDateTextView.text = data[position].dueDate
        holder.createDateTextView.text= data[position].creationDate
        holder.dueTextView.text="Due:"


        holder.itemView.setOnClickListener {
            //println(data[position].title)

            var intent= Intent(holder.itemView.context,Details::class.java)
            //var t = data[position]
            intent.putExtra("task",data[position])

            holder.itemView.context.startActivity(intent)
        }


        if (!data[position].stauts) {

            holder.checkState.isChecked= false
            holder.taskTitleTextView.paintFlags =
                holder.taskTitleTextView.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else {
            holder.checkState.isChecked
            holder.taskTitleTextView.paintFlags =
                holder.taskTitleTextView.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
            holder.taskTitleTextView.setTextColor(Color.parseColor("#807f7e"))
        }

        holder.checkState.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {

                db.collection("users").document(uid).collection("task").document(data[position].id!!)
                //db.collection("Tasks").document(data[position].id!!)
                    .update("compeleted", true)
                holder.taskTitleTextView.paintFlags =
                    holder.taskTitleTextView.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
                holder.taskTitleTextView.setTextColor(Color.parseColor("#6e6d6a"))
            } else {
                db.collection("users").document(uid).collection("task").document(data[position].id!!)
                    //db.collection("Tasks").document(data[position].id!!)
                    .update("compeleted", false)
                !compoundButton.isChecked
                holder.taskTitleTextView.paintFlags =
                    holder.taskTitleTextView.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        var currentDateTime = LocalDate.now()
        var day =
            "${currentDateTime.dayOfMonth}/${currentDateTime.month.value}/${currentDateTime.year}"

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        sdf.format(Date())
        var parsedDateLocal:Date = sdf.parse(day)
        var parsedDateDue:Date = sdf.parse(data[position].dueDate)



        if((parsedDateDue.before(parsedDateLocal)) && data[position].stauts==false ){
             holder.taskDueDateTextView.setTextColor(Color.parseColor("#C61313"))
            holder.dueTextView.setTextColor(Color.parseColor("#C61313"))


        }


        holder.deleteCardView.setOnClickListener {
            var deleteDialog = AlertDialog.Builder(holder.deleteCardView.context)
                .setTitle("Delete")
                .setMessage("Do you want to delete?")
                .setPositiveButton("Yes") { dialog, which ->
                    db.collection("users").document(uid).collection("task").document(data[position].id!!)

                        //db.collection("Tasks").document(data[position].id!!)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                holder.deleteCardView.context,
                                "Deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            //data.removeAt(position)
                            notifyDataSetChanged()
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
            TasktitleEditText.text=Editable.Factory.getInstance().newEditable(data[position].title)

            var TaskdescrptionEditText = u.findViewById<EditText>(R.id.EditTextTaskNote)
            TaskdescrptionEditText.text=Editable.Factory.getInstance().newEditable(data[position].descrption)


            data[position].title=TasktitleEditText.text.toString()
            data[position].descrption=TaskdescrptionEditText.text.toString()
            data[position].dueDate=holder.taskDueDateTextView.text.toString()
            data[position].stauts
            updateDialogBtn.setOnClickListener {
                db.collection("users").document(uid).collection("task").document(data[position].id!!)
                    //db.collection("Tasks").document(data[position].id!!)
                    .update(
                        mapOf
                        ("title" to TasktitleEditText.text.toString(),
                        "descrption" to TaskdescrptionEditText.text.toString(),
                        "dueDate" to holder.taskDueDateTextView.text.toString(),
                        "compeleted" to data[position].stauts)
                   )
                    .addOnSuccessListener {
                        Toast.makeText(
                            holder.updateImage.context,
                            "Task has been updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        //var cont: Context = Home::class.java as Context
//                        var i=Intent(holder.updateImage.context,Home::class.java)
//                        holder.updateImage.context.startActivity(i)
//
//
//                        notifyItemChanged(position)
//                        notifyDataSetChanged( )

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
    //var taskNoteTextView = v.findViewById<TextView>(R.id.textViewTaskNote)
    var taskDueDateTextView = v.findViewById<TextView>(R.id.textViewDueDate)
    var createDateTextView = v.findViewById<TextView>(R.id.textViewCreationDate)
    var updateImage = v.findViewById<ImageView>(R.id.imageViewUpdate)
    var deleteCardView = v.findViewById<CardView>(R.id.deleteCardview)
    var dueTextView= v.findViewById<TextView>(R.id.DueTextView)
    var checkState = v.findViewById<CheckBox>(R.id.checkBoxState)


}