package com.wspyo.sogating.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wspyo.sogating.MainActivity
import com.wspyo.sogating.R

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val Tag = JoinActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)


        auth = Firebase.auth

        val email : TextInputEditText = findViewById(R.id.emailArea)
        val password : TextInputEditText = findViewById(R.id.passwordArea)


        findViewById<Button>(R.id.joinBtn).setOnClickListener{

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this,MainActivity::class.java)

                        val user = auth.currentUser
                        Log.d(Tag,user?.uid.toString())

                        startActivity(intent)
                    } else {
                        Toast.makeText(this,"회원가입 실패",Toast.LENGTH_LONG).show()
                    }
                }

        }


    }
}