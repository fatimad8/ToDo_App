package com.example.todo_app

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
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
        val db = Firebase.firestore

        var addButton = findViewById<FloatingActionButton>(R.id.fabAdd)

        var mToolbar = findViewById<Toolbar>(R.id.mToolbar1)
        mToolbar.title = "ToDo App"
        mToolbar.setTitleTextColor(Color.WHITE)
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        setSupportActionBar(mToolbar)
        mToolbar.setNavigationOnClickListener {
            finish()
        }

        var currentDateTime = LocalDate.now()
        var day =
            "${currentDateTime.dayOfMonth}/${currentDateTime.month.value}/${currentDateTime.year}"


        var dialog = layoutInflater.inflate(R.layout.task_dialog, null)
        var updateDialog = layoutInflater.inflate(R.layout.update_dialog, null)
        var row_task = layoutInflater.inflate(R.layout.list_row_task, null)

        var dateTextView = row_task.findViewById<TextView>(R.id.textViewDueDate)


        var homeModel = HomeViewModel()
        var mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        homeModel.getAllTask().observe(this, { list ->
            mRecyclerView.adapter = Task_Adapter(list)
            mRecyclerView.adapter?.notifyDataSetChanged()

        })

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
                        "creationDate" to day,
                        "compeleted" to false
                    )

                    db.collection("Tasks")
                        .add(task)
                        .addOnSuccessListener { t ->
                            Toast.makeText(
                                this,
                                "Task has been added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                }
                taskDialog.dismiss()
                onResume()
                mRecyclerView.adapter?.notifyDataSetChanged()

            }

        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        when (item.itemId) {
            R.id.alphaItem -> {
                var homevm = HomeViewModel()
                homevm.sortTask().observe(this, { list ->

                    mRecyclerView.adapter = Task_Adapter(list)
                    mRecyclerView.adapter?.notifyDataSetChanged()
                })
            }
            R.id.filterItem -> {
                var homevm = HomeViewModel()
                homevm.filterTask().observe(this, { list ->

                    mRecyclerView.adapter = Task_Adapter(list)
                    mRecyclerView.adapter?.notifyDataSetChanged()
                })
            }

        }

        return super.onOptionsItemSelected(item)
    }
}


