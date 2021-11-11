package com.example.todo_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var textViewLogin= findViewById<TextView>(R.id.textViewLogin )
        var fname= findViewById<EditText>(R.id.EditTextName)
        var regEmail=findViewById<EditText>(R.id.EditTextRegEmail)
        var regPass= findViewById<EditText>(R.id.EditTextRegPass)
        var regButton= findViewById<Button>(R.id.buttonSignUp)

        textViewLogin.setOnClickListener {
            var intent= Intent(this,Login::class.java)
            startActivity(intent)
        }

    regButton.setOnClickListener {

        var auth= Firebase.auth

        auth.createUserWithEmailAndPassword(regEmail.text.toString(),regPass.text.toString())
            .addOnCompleteListener { task->

                if(task.isSuccessful){

                    println("User has been registered successfully with UID "+ auth.currentUser?.uid)

                    val u = hashMapOf(
                        "email" to auth.currentUser?.email,
                        "firstname" to fname.text.toString()

                    )

                    var db= Firebase.firestore

                    db.collection("users").document(auth.currentUser?.uid.toString())
                        .set(u)
                    var intent = Intent(this, Login::class.java)
                    startActivity(intent)

                }else{

                    println("Error")
                }
            }.addOnFailureListener {
                println(it.message)
            }
    }



    }


}