package com.wspyo.sogating

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wspyo.sogating.auth.IntroActivity
import com.wspyo.sogating.auth.UserDataModel
import com.wspyo.sogating.setting.SettingActivity
import com.wspyo.sogating.slider.CardStackAdapter
import com.wspyo.sogating.utils.FirebaseAuthUtils
import com.wspyo.sogating.utils.FirebaseRef
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager

    private var TAG = MainActivity::class.java.simpleName

    private val usersDataList = mutableListOf<UserDataModel>()

    private val uid = FirebaseAuthUtils.getUid()

    private lateinit var currencyUserGender: String

    private var userCount : Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)
        val settingIcon = findViewById<ImageView>(R.id.settingIcon)


        settingIcon.setOnClickListener{

            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {

                if(direction == Direction.Right){
                    Toast.makeText(this@MainActivity,"right",Toast.LENGTH_SHORT).show()
                    userLikeOtherUser(uid,usersDataList[userCount]?.uid.toString())
                }
                if(direction == Direction.Left){
                    Toast.makeText(this@MainActivity,"left",Toast.LENGTH_SHORT).show()
                }

                userCount = userCount + 1

               if(userCount == usersDataList.count()){
                   getUserDataList(currencyUserGender)
                    Toast.makeText(this@MainActivity,"fetch newData",Toast.LENGTH_SHORT).show()
               }
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })


        cardStackAdapter = CardStackAdapter(baseContext,usersDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

        getMyUserData()
//        getUserDataList()


    }

    private fun getMyUserData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                currencyUserGender = data?.gender.toString()
                getUserDataList(currencyUserGender)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid!!).addValueEventListener(postListener)
    }

    private fun getUserDataList(currencyUserGender : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(user!!.gender.toString().equals(currencyUserGender)){
                    }else{
                        usersDataList.add(user!!)
                    }
                }
                // 데이터 동기화
                cardStackAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    private fun userLikeOtherUser(uid:String,otherUid:String){

        FirebaseRef.userLikeRef.child(uid).child(otherUid).setValue("true")

    }
}