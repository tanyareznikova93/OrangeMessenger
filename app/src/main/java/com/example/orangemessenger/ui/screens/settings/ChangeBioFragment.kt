package com.example.orangemessenger.ui.screens.settings

import com.example.orangemessenger.R
import com.example.orangemessenger.database.USER
import com.example.orangemessenger.database.setBioToDatabase
import com.example.orangemessenger.ui.screens.base.BaseChangeFragment
import kotlinx.android.synthetic.main.fragment_change_bio.*

//Фрагмент для изменения информации о пользователе
class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)
    }//onResume

    override fun change() {
        super.change()
        val newBio:String = settings_input_bio.text.toString()
        setBioToDatabase(newBio)
    }//change

}//ChangeBioFragment