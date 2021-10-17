package com.example.orangemessenger.ui.screens.base

import android.view.*
import androidx.fragment.app.Fragment
import com.example.orangemessenger.MainActivity
import com.example.orangemessenger.R
import com.example.orangemessenger.utilits.hideKeyboard

open class BaseChangeFragment(layout:Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        (activity as MainActivity).mAppDrawer.disableDrawer()
        //APP_ACTIVITY.mAppDrawer.disableDrawer()
        hideKeyboard()

    }

    override fun onStop() {
        super.onStop()
        //(activity as MainActivity).mAppDrawer.enableDrawer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm, menu)
        //APP_ACTIVITY.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    open fun change() {

    }

}