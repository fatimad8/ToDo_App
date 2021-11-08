package com.example.todo_app

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        var mSearchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.mSearchView)
        var mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        var addButton= findViewById<FloatingActionButton>(R.id.fabAdd)
        var currentDateTime = LocalDate.now()
        var day = currentDateTime.dayOfMonth


        val db = Firebase.firestore
        var taskList= mutableListOf<Task>()

        db.collection("Tasks")
            .get()
            .addOnSuccessListener { result : QuerySnapshot ->
                for (document in result){
                    taskList.add(
                        Task(
                            document.getString("title")!!,document.getString("descrption")!!,document.getString("dueDate")!!,false)
                    )

                }
                mRecyclerView.adapter=Task_Adapter(taskList)
                mRecyclerView.adapter?.notifyDataSetChanged()
            }

        mRecyclerView.layoutManager = LinearLayoutManager(this)






        addButton.setOnClickListener {
            var dialog = layoutInflater.inflate(R.layout.task_dialog,null)
            var row_task = layoutInflater.inflate(R.layout.list_row_task,null)
            var dateTextView= row_task.findViewById<TextView>(R.id.textViewDueDate)



            var taskDialog = AlertDialog.Builder(this)
                .setView(dialog)
                .show()

            var dateImageView=dialog.findViewById<ImageView>(R.id.imageViewDate)

            dateImageView.setOnClickListener {
                var c = Calendar.getInstance()
                var year = c.get(Calendar.YEAR)
                var month = c.get(Calendar.MONTH)
                var day = c.get(Calendar.DAY_OF_MONTH)

                var DatePickerDialog= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                    dateTextView.text= "$dayOfMonth/${month+1}/$year"


                },year,month,day)


                DatePickerDialog.show()

            }


            var addDialogBtn=dialog.findViewById<Button>(R.id.buttonAdd)
            var TasktitleEditText=dialog.findViewById<EditText>(R.id.EditTextTaskTitle)
            var TaskdescrptionEditText= dialog.findViewById<EditText>(R.id.EditTextTaskNote)

            addDialogBtn.setOnClickListener {

                var title=TasktitleEditText.getText().toString()
                var desccrption= TaskdescrptionEditText.getText().toString()
                if(!title.isEmpty()&& !desccrption.isEmpty()){
                    var task= hashMapOf(
                        "title" to title,
                        "descrption" to desccrption,
                        "dueDate" to  dateTextView.text,
                        //"creationDate" to currentDateTime.toString().toLong()
                    )

                    db.collection("Tasks")
                        .add(task)
                        .addOnSuccessListener { t->
                            Toast.makeText(this, "Task has been added successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                }
                taskDialog.dismiss()

            }

        }



    }}