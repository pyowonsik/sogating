package com.wspyo.sogating

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.wspyo.sogating.R
import com.wspyo.sogating.auth.IntroActivity
import com.wspyo.sogating.auth.JoinActivity
import com.wspyo.sogating.utils.FirebaseAuthUtils

class SplashActivity : AppCompatActivity() {
    val Tag = SplashActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val uid = FirebaseAuthUtils.getUid()

        Log.d(Tag,uid)

        if(uid == "null"){
            Handler().postDelayed({
                startActivity(Intent(this,IntroActivity::class.java))
                finish()
            },3000)
        }
        else{
            Handler().postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            },3000)
        }


    }
}