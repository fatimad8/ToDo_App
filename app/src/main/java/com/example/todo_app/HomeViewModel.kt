package com.example.todo_app

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View.inflate
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo_app.model.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeViewModel:ViewModel() {


    fun getAllTask(): MutableLiveData<MutableList<Task>> {
        var mutableLiveData = MutableLiveData<MutableList<Task>>()
        val db = Firebase.firestore
        var taskList = mutableListOf<Task>()

        db.collection("Tasks")
            .addSnapshotListener() { result, e ->
                if (result != null) {
                    taskList.clear()
                    for (document in result) {
                        taskList.add(
                            Task(
                                document.id,
                                document.getString("title")!!,
                                document.getString("descrption")!!,
                                document.getString("dueDate")!!,
                                document.getString("creationDate")!!,
                                document.getBoolean("compeleted")!!
                            )
                        )
                    }

                }

                mutableLiveData.postValue(taskList)
            }
        return mutableLiveData

    }


    fun sortTask(): MutableLiveData<MutableList<Task>> {
        var mutableLiveData = MutableLiveData<MutableList<Task>>()
        val db = Firebase.firestore
        var taskList = mutableListOf<Task>()

        db.collection("Tasks").orderBy("title", Query.Direction.ASCENDING)
            .addSnapshotListener() { result, e ->
                if (result != null) {
                    taskList.clear()
                    for (document in result) {
                        taskList.add(
                            Task(
                                document.id,
                                document.getString("title")!!,
                                document.getString("descrption")!!,
                                document.getString("dueDate")!!,
                                document.getString("creationDate")!!,
                                document.getBoolean("compeleted")!!
                            )
                        )
                    }
                    mutableLiveData.postValue(taskList)
                }

            }
        return mutableLiveData
    }


    fun filterTask(): MutableLiveData<MutableList<Task>> {
        var mutableLiveData = MutableLiveData<MutableList<Task>>()
        val db = Firebase.firestore
        var taskList = mutableListOf<Task>()
        db.collection("Tasks").whereEqualTo("compeleted", false)
            .addSnapshotListener() { result, e ->
                if (result != null) {
                    taskList.clear()
                    for (document in result) {
                        taskList.add(
                            Task(
                                document.id,
                                document.getString("title")!!,
                                document.getString("descrption")!!,
                                document.getString("dueDate")!!,
                                document.getString("creationDate")!!,
                                document.getBoolean("compeleted")!!
                            )
                        )
                     }
                    mutableLiveData.postValue(taskList)
                }

            }
        return mutableLiveData
    }

}







