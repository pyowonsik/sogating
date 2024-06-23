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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wspyo.sogating.MainActivity
import com.wspyo.sogating.R
import com.wspyo.sogating.utils.FirebaseRef

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val Tag = JoinActivity::class.java.simpleName

    private var nickname = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth


        findViewById<Button>(R.id.joinBtn).setOnClickListener{

            val email : TextInputEditText = findViewById(R.id.emailArea)
            val password : TextInputEditText = findViewById(R.id.passwordArea)

            nickname  = findViewById<TextInputEditText>(R.id.nicknameArea).text.toString()
            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()
            city = findViewById<TextInputEditText>(R.id.cityArea).text.toString()
            age  = findViewById<TextInputEditText>(R.id.ageArea).text.toString()

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        val uid = user?.uid.toString()
                        // user -> uid -> UserModel
                        val userDataModel = UserDataModel(
                            uid,
                            nickname,
                            gender,
                            city,
                            age
                        )
                        FirebaseRef.userInfoRef.child(uid).setValue(userDataModel)

                        Toast.makeText(this,"회원가입 성공",Toast.LENGTH_LONG).show()
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this,"회원가입 실패",Toast.LENGTH_LONG).show()
                    }
                }

        }

    }
}