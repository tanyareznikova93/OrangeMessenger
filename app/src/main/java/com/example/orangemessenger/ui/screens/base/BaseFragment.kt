package com.example.orangemessenger.ui.screens.base

import androidx.fragment.app.Fragment
import com.example.orangemessenger.utilits.APP_ACTIVITY

open class BaseFragment(layout:Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

}