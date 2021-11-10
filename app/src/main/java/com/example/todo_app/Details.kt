package com.example.todo_app

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.todo_app.model.Task

class Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        var mToolbar= findViewById<Toolbar>(R.id.mToolbar1)
        var note= findViewById<TextView>(R.id.textViewNote)
        var due= findViewById<TextView>(R.id.textViewDue)
        var create= findViewById<TextView>(R.id.textViewcreate)
        var status1= findViewById<TextView>(R.id.textViewStatus)

        var task= intent.getSerializableExtra("task") as Task


        note.text=task.descrption
        mToolbar.title=task.title
        due.text=task.dueDate


        if(task.stauts){
            status1.text="Completed"
        }else if(task.stauts==false){
            status1.text="UnComplete"
        }
         create.text=task.creationDate
        mToolbar.setTitleTextColor(Color.WHITE)
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        mToolbar.setNavigationOnClickListener {
           finish()
        }
    }
}