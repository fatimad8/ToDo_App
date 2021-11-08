package com.example.todo_app

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.model.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.util.*


class Home : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        var taskList = mutableListOf<Task>()

        var mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        mRecyclerView.adapter=Task_Adapter(taskList)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        var addButton = findViewById<FloatingActionButton>(R.id.fabAdd)

        var currentDateTime = LocalDate.now()
        var day = currentDateTime.dayOfMonth


        var dialog = layoutInflater.inflate(R.layout.task_dialog, null)
        var updateDialog= layoutInflater.inflate(R.layout.update_dialog, null)
        var row_task = layoutInflater.inflate(R.layout.list_row_task, null)

        var dateTextView = row_task.findViewById<TextView>(R.id.textViewDueDate)
       // var updateImage= row_task.findViewById<ImageView>(R.id.imageViewUpdate)



        val db = Firebase.firestore


        db.collection("Tasks")
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                for (document in result) {
                    taskList.add(
                        Task(
                            document.id,
                            document.getString("title")!!,
                            document.getString("descrption")!!,
                            document.getString("dueDate")!!,
                            document.getBoolean("compeleted")!!
                        )
                    )

                }

                mRecyclerView.adapter = Task_Adapter(taskList)
                mRecyclerView.adapter?.notifyDataSetChanged()
            }


        addButton.setOnClickListener {


            var taskDialog = AlertDialog.Builder(this)
                .setView(dialog)
                .show()

            var dateImageView = dialog.findViewById<ImageView>(R.id.imageViewDate)

            dateImageView.setOnClickListener {
                var c = Calendar.getInstance()
                var year = c.get(Calendar.YEAR)
                var month = c.get(Calendar.MONTH)
                var day = c.get(Calendar.DAY_OF_MONTH)

                var DatePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                        dateTextView.text = "$dayOfMonth/${month + 1}/$year"


                    },
                    year,
                    month,
                    day
                )


                DatePickerDialog.show()

            }


            var addDialogBtn = dialog.findViewById<Button>(R.id.buttonUpdate)
            var TasktitleEditText = dialog.findViewById<EditText>(R.id.EditTextTaskTitle)
            var TaskdescrptionEditText = dialog.findViewById<EditText>(R.id.EditTextTaskNote)

            addDialogBtn.setOnClickListener {

                var title = TasktitleEditText.getText().toString()
                var desccrption = TaskdescrptionEditText.getText().toString()
                if (!title.isEmpty() && !desccrption.isEmpty()) {
                    var task = hashMapOf(
                        "title" to title,
                        "descrption" to desccrption,
                        "dueDate" to dateTextView.text,
                        "compeleted" to false
                        //"creationDate" to currentDateTime.toString().toLong()
                    )

                    db.collection("Tasks")
                        .add(task)
                        .addOnSuccessListener { t ->
                            Toast.makeText(
                                this,
                                "Task has been added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            mRecyclerView.adapter?.notifyDataSetChanged()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                }
                taskDialog.dismiss()

            }

        }




//        updateImage.bringToFront()
//
//        updateImage.setOnClickListener {
//            var intent=Intent(this,Update::class.java)
//            startActivity(intent)
//        }

//        updateImage.setOnClickListener {
//
//            var uDialog = AlertDialog.Builder(this)
//                .setView(updateDialog)
//                .show()
//
//            var dateImageView = updateDialog.findViewById<ImageView>(R.id.imageViewDate)
//
//            dateImageView.setOnClickListener {
//                var c = Calendar.getInstance()
//                var year = c.get(Calendar.YEAR)
//                var month = c.get(Calendar.MONTH)
//                var day = c.get(Calendar.DAY_OF_MONTH)
//
//                var DatePickerDialog = DatePickerDialog(
//                    this,
//                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//
//                        dateTextView.text = "$dayOfMonth/${month + 1}/$year"
//                    },
//                    year,
//                    month,
//                    day
//                )
//                DatePickerDialog.show()
//            }
//            var updateDialogBtn = updateDialog.findViewById<Button>(R.id.buttonUpdate)
//            var TasktitleEditText = updateDialog.findViewById<EditText>(R.id.EditTextTaskTitle)
//            var TaskdescrptionEditText = updateDialog.findViewById<EditText>(R.id.EditTextTaskNote)
//
//            updateDialogBtn.setOnClickListener {
//
//                db.collection("Tasks")
//                    .document()
//                    .update(mapOf("title" to TasktitleEditText.text,
//                        "descrption" to TaskdescrptionEditText,
//                        "dueDate" to dateTextView,
//                        "compeleted" to true)
//                    )
//                    .addOnSuccessListener {
//                        Toast.makeText(
//                            this,
//                            "Task has been updated successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        mRecyclerView.adapter?.notifyDataSetChanged()
//                    }
//                    .addOnFailureListener {e->
//                        Log.w(TAG, "Error updating document", e)
//                    }
//               uDialog.dismiss()
//
//              }
//         }


    }

    override fun onRestart() {
        super.onRestart()
        val db = Firebase.firestore
        var taskList = mutableListOf<Task>()
        var mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)

        mRecyclerView.layoutManager = LinearLayoutManager(this)


        db.collection("Tasks")
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                for (document in result) {
                    taskList.add(
                        Task(
                            document.id,
                            document.getString("title")!!,
                            document.getString("descrption")!!,
                            document.getString("dueDate")!!,
                            document.getBoolean("compeleted")!!
                        )
                    )

                }
                 mRecyclerView.adapter=Task_Adapter(taskList)
                 mRecyclerView.adapter?.notifyDataSetChanged()
            }

    }

    override fun onResume() {
        super.onResume()
        var row_task = layoutInflater.inflate(R.layout.list_row_task, null)
        var checkbox= row_task.findViewById<CheckBox>(R.id.checkBoxState)
        val db = Firebase.firestore
        var mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        var taskList = mutableListOf<Task>()

        mRecyclerView.layoutManager = LinearLayoutManager(this)


        db.collection("Tasks")
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                for (document in result) {
                    taskList.add(
                        Task(
                            document.id,
                            document.getString("title")!!,
                            document.getString("descrption")!!,
                            document.getString("dueDate")!!,
                            document.getBoolean("compeleted")!!
                        )
                    )
                }
                mRecyclerView.adapter=Task_Adapter(taskList)
                mRecyclerView.adapter?.notifyDataSetChanged()

            }

    }
}