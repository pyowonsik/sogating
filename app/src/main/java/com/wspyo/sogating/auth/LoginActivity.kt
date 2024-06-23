package com.wspyo.sogating.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

class LoginActivity : AppCompatActivity() {



    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val loginBtn : Button = findViewById(R.id.loginBtn)
        val email : TextInputEditText = findViewById(R.id.emailArea)
        val password : TextInputEditText = findViewById(R.id.passwordArea)

        loginBtn.setOnClickListener{
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this,"로그인 실패",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }


}