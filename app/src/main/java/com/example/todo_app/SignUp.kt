package com.example.todo_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var textViewLogin= findViewById<TextView>(R.id.textViewLogin )
        var fname= findViewById<EditText>(R.id.EditTextName)
        var RegEmail=findViewById<EditText>(R.id.EditTextRegEmail)
        var RegPass= findViewById<EditText>(R.id.EditTextRegPass)
        var RegButton= findViewById<Button>(R.id.buttonSignUp)

        textViewLogin.setOnClickListener {
            var intent= Intent(this,Login::class.java)
            startActivity(intent)
        }
    }
}