package com.wspyo.sogating

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import com.wspyo.sogating.auth.IntroActivity
import com.wspyo.sogating.auth.UserDataModel
import com.wspyo.sogating.slider.CardStackAdapter
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)
        val settingIcon = findViewById<ImageView>(R.id.settingIcon)


        settingIcon.setOnClickListener{
            val auth = Firebase.auth
            auth.signOut()
            val intent = Intent(this,IntroActivity::class.java)
            startActivity(intent)
        }

        manager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
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


        getUserDataList()


    }
    private fun getUserDataList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    usersDataList.add(user!!)
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
}