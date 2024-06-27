package com.wspyo.sogating.setting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wspyo.sogating.R
import com.wspyo.sogating.auth.UserDataModel
import com.wspyo.sogating.utils.FirebaseAuthUtils
import com.wspyo.sogating.utils.FirebaseRef

class MyPageActivity : AppCompatActivity() {


    private val TAG = MyPageActivity::class.java.simpleName

    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        getMyData()
    }

    private fun getMyData(){

        val myImage = findViewById<ImageView>(R.id.myImage)

        val myUid = findViewById<TextView>(R.id.myUid)
        val myNickname = findViewById<TextView>(R.id.myNickname)
        val myAge = findViewById<TextView>(R.id.myAge)
        val myCity = findViewById<TextView>(R.id.myCity)
        val myGender = findViewById<TextView>(R.id.myGender)


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.getValue(UserDataModel::class.java)

                    myUid.text = data!!.uid
                    myNickname.text = data.nickname
                    myAge.text = data.age
                    myCity.text = data.city
                    myGender.text = data.gender

                val storageRef = Firebase.storage.reference.child(data.uid + ".png")
                storageRef.downloadUrl.addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Glide.with(baseContext)
                            .load(task.result)
                            .into(myImage)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid!!).addValueEventListener(postListener)

    }
}