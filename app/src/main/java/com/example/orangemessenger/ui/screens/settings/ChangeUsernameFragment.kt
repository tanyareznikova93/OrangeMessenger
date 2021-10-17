package com.example.orangemessenger.ui.screens.settings

import com.example.orangemessenger.R
import com.example.orangemessenger.database.*
import com.example.orangemessenger.ui.screens.base.BaseChangeFragment
import com.example.orangemessenger.utilits.*
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername:String

    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)
    }

    override fun change() {

        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if(mNewUsername.isEmpty()){
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener{
                    if(it.hasChild(mNewUsername)){
                        showToast("Пользователь с таким именем уже существует")
                    } else {
                        changeUsername()
                    }
                })
        }

    }//change()

    //Изменение username в базе данных
    private fun changeUsername() {

        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    updateCurrentUsername(mNewUsername)
                }
            }

    }//changeUsername()

}//ChangeUsernameFragment