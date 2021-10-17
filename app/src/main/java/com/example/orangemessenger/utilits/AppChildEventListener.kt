package com.example.orangemessenger.utilits

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//Модификация класса ValueEventListener
class AppChildEventListener (val onSuccess:(DataSnapshot) -> Unit): ChildEventListener {

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        onSuccess(snapshot)
    }//onChildAdded

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        //TODO("Not yet implemented")
    }//onChildChanged

    override fun onChildRemoved(snapshot: DataSnapshot) {
        //TODO("Not yet implemented")
    }//onChildRemoved

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        //TODO("Not yet implemented")
    }//onChildMoved

    override fun onCancelled(error: DatabaseError) {
        //TODO("Not yet implemented")
    }//onCancelled


}//AppChildEventListener