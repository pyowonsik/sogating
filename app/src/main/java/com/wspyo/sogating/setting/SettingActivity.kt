package com.wspyo.sogating.setting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wspyo.sogating.R
import com.wspyo.sogating.auth.IntroActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        findViewById<Button>(R.id.myPageBtn).setOnClickListener{
            val intent = Intent(this,MyPageActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.logoutBtn).setOnClickListener{
            val auth = Firebase.auth
            auth.signOut()

            val intent = Intent(this,IntroActivity::class.java)
            startActivity(intent)
        }

    }
}