package com.wspyo.sogating.utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthUtils {


    companion object{

        private lateinit var auth : FirebaseAuth

        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            // auth = Firebase.auth

            return auth.currentUser?.uid.toString()
        }
    }


}