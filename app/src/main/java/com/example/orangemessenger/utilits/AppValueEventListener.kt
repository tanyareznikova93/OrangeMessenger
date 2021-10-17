package com.example.orangemessenger.utilits

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//Модификация класса ValueEventListener
class AppValueEventListener (val onSuccess:(DataSnapshot) -> Unit):ValueEventListener{

    override fun onDataChange(snapshot: DataSnapshot) {

        onSuccess(snapshot)

    }

    override fun onCancelled(error: DatabaseError) {

    }

}//AppValueEventListener
