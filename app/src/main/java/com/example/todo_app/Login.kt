package com.example.todo_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var textViewSignUp = findViewById<TextView>(R.id.textViewSignUp)
        var logEmail = findViewById<EditText>(R.id.EditTextEmail)
        var logPass = findViewById<EditText>(R.id.EditTextPass)
        var loginButton = findViewById<Button>(R.id.buttonLogin)

        textViewSignUp.setOnClickListener {
            var intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }



        loginButton.setOnClickListener {

            var auth = Firebase.auth

            auth.signInWithEmailAndPassword(logEmail.text.toString(), logPass.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var user = auth.currentUser
                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                        println("Login Success " + user?.uid)
                        var intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    } else {

                        Toast.makeText(
                            this,
                            "Login Failed, You don't have account",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }


        }


    }}
