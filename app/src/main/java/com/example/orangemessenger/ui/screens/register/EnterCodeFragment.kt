package com.example.orangemessenger.ui.screens.register

import androidx.fragment.app.Fragment
import com.example.orangemessenger.R
import com.example.orangemessenger.database.*
import com.example.orangemessenger.utilits.*
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

//Фрагмент для ввода кода подтверждения при регистрации
class EnterCodeFragment(val phoneNumber: String, val id: String) : Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        register_input_code_et.addTextChangedListener(AppTextWatcher{

                val string:String = register_input_code_et.text.toString()
                    //if(string.length == 5){
            if(string.length == 6){
                    enterCode()
            }
        })
    }

    //Функция проверяет код, если все нормально, производит создания информации о пользователе в базе данных
    private fun enterCode() {

        val code :String = register_input_code_et.text.toString()
        val credential :PhoneAuthCredential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val uid:String = AUTH.currentUser?.uid.toString()
                val dateMap:MutableMap<String,Any> = mutableMapOf<String,Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                    .addListenerForSingleValueEvent(AppValueEventListener{

                        if(!it.hasChild(CHILD_USERNAME)){
                            dateMap[CHILD_USERNAME] = uid
                        }//if

                        REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                            .addOnFailureListener { showToast(it.message.toString()) }
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                                    .updateChildren(dateMap)
                                    .addOnSuccessListener {
                                        showToast("Добро пожаловать")
                                        restartActivity()
                                    }
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                    })

            } else showToast(task.exception?.message.toString())
        }


    }

}