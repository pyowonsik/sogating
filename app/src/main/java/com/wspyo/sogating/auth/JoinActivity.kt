package com.wspyo.sogating.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wspyo.sogating.MainActivity
import com.wspyo.sogating.R
import com.wspyo.sogating.utils.FirebaseRef
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val TAG = JoinActivity::class.java.simpleName

    private var nickname = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private var uid = ""

    private lateinit var profileImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth

        // 갤러리에 접근해서 선택하 사진을 imageArea에 넣는다.
         profileImage = findViewById(R.id.imageArea)
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback{ uri ->
                profileImage.setImageURI(uri)
            }
        )

        profileImage.setOnClickListener{
            getAction.launch("image/*")
        }
        //


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
                        uid = user?.uid.toString()
                        // user -> uid -> UserModel
                        val userDataModel = UserDataModel(
                            uid,
                            nickname,
                            gender,
                            city,
                            age
                        )
                        FirebaseRef.userInfoRef.child(uid).setValue(userDataModel)
                        uploadImage(uid)

                        Toast.makeText(this,"회원가입 성공",Toast.LENGTH_LONG).show()
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this,"회원가입 실패",Toast.LENGTH_LONG).show()
                    }
                }

        }

    }

    private fun uploadImage(uid : String){

        val storage = Firebase.storage
        val storageRef = storage.reference.child(uid + ".png")


        // Get the data from an ImageView as bytes
        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }


}