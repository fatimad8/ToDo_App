package com.example.todo_app

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Update : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

         var row_task = layoutInflater.inflate(R.layout.list_row_task, null)


        var TaskTitle=findViewById<EditText>(R.id.taskTitleEditText)
        //TaskTitle.text=Editable.Factory.getInstance().newEditable()
        var TaskNote=findViewById<EditText>(R.id.TaskNoteEditText)
        var TaskDue=findViewById<EditText>(R.id.taskDueDateEditText)
        var updateButton=findViewById<Button>(R.id.updateButton)
        var udpateImage=row_task.findViewById<ImageView>(R.id.imageViewUpdate)

        val db = Firebase.firestore
//
//        udpateImage.setOnClickListener {
//
//            updateButton.setOnClickListener {
//                db.collection("Tasks")
//                    .document(taskId.toString())
//                    .update(mapOf("title" to TaskTitle.text,
//                        "descrption" to TaskNote,
//                        "dueDate" to TaskDue,
//                        "compeleted" to true)
//                    )
//                    .addOnSuccessListener {
//                        Toast.makeText(
//                            this,
//                            "Task has been updated successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    .addOnFailureListener {e->
//                        Log.w(ContentValues.TAG, "Error updating document", e)
//                    }
//            }
//        }






    }
}