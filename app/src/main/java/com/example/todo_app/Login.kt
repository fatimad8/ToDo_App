package com.example.todo_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var textViewSignUp= findViewById<TextView>(R.id.textViewSignUp)
        var logEmail=findViewById<EditText>(R.id.EditTextName)
        var logPass= findViewById<EditText>(R.id.EditTextPass)
        var loginButton= findViewById<Button>(R.id.buttonLogin)

        textViewSignUp.setOnClickListener {
            var intent= Intent(this,SignUp::class.java)
            startActivity(intent)
        }




     }
}