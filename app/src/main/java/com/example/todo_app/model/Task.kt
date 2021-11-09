package com.example.todo_app.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Task(var id:String?=null,var title:String, var descrption:String, var dueDate:String,var creationDate:String,var stauts:Boolean):Serializable {
}