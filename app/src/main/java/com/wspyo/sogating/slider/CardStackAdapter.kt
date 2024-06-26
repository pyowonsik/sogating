package com.wspyo.sogating.slider

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wspyo.sogating.R
import com.wspyo.sogating.auth.UserDataModel

class CardStackAdapter(val context : Context, val items : List<UserDataModel>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.item_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardStackAdapter.ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){


        val image : ImageView = view.findViewById(R.id.profileImageArea)

        // view = item_card
        val nickname : TextView = view.findViewById(R.id.itemNickname)
        val age : TextView = view.findViewById(R.id.itemAge)
        val city : TextView = view.findViewById(R.id.itemCity)

        // data = user data
        fun binding(data : UserDataModel){

            // firebase storage의 해당 ref에서 데이터를 가져와 image로 사용
            val storageRef = Firebase.storage.reference.child(data.uid + ".png")
            storageRef.downloadUrl.addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Glide.with(context)
                        .load(task.result)
                        .into(image)
                }
            }

            nickname.text = data.nickname
            age.text = data.age
            city.text = data.city
        }
    }
}