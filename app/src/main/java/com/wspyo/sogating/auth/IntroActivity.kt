package com.wspyo.sogating.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wspyo.sogating.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val joinBtn : Button = findViewById(R.id.joinBtn)
        joinBtn.setOnClickListener{
            val intent = Intent(this,JoinActivity::class.java)
            startActivity(intent)
        }


        val loginBtn : Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}