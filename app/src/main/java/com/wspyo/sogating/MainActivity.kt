package com.wspyo.sogating

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
//                    Toast.makeText(this@MainActivity,"right",Toast.LENGTH_SHORT).show()
                    userLikeOtherUser(uid,usersDataList[userCount]?.uid.toString())
                }
                if(direction == Direction.Left){
//                    Toast.makeText(this@MainActivity,"left",Toast.LENGTH_SHORT).show()
                }

                userCount = userCount + 1

               if(userCount == usersDataList.count()){
                   getUserDataList(currencyUserGender)
//                    Toast.makeText(this@MainActivity,"fetch newData",Toast.LENGTH_SHORT).show()
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

        getOtherUserLikeList(otherUid)

    }

    private fun getOtherUserLikeList(otherUid: String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children){
                    val likeUserKey = dataModel.key.toString()
                    if(likeUserKey.equals(uid)){
                        Toast.makeText(this@MainActivity,"매칭완료",Toast.LENGTH_SHORT).show()
                        createNotificationChannel()
                        sendNotification()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)
    }


    // Notification
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun sendNotification(){
        var builder = NotificationCompat.Builder(this, "Test_Channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("매칭완료")
            .setContentText("매칭이 완료 되었습니다. 저사람도 나를 좋아해요")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//        with(NotificationManagerCompat.from(this)){
//            notify(123, builder.build())
//        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(123, builder.build())
    }
}