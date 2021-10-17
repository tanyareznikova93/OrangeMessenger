package com.example.orangemessenger.utilits

import com.example.orangemessenger.database.*

//Класс перечисление состояний приложения
enum class AppStates(val state:String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает...");

    companion object{
        //Функция принимает состояние и записывает в базу данных
        fun updateState(appStates:AppStates){

            if(AUTH.currentUser != null){
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener { USER.state = appStates.state }
                    .addOnFailureListener { showToast(it.message.toString()) }
            }//if

        }//updateState
    }
}//AppStates