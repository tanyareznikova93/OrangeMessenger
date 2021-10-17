package com.example.orangemessenger.ui.screens.groups

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.example.orangemessenger.R
import com.example.orangemessenger.database.*
import com.example.orangemessenger.models.CommonModel
import com.example.orangemessenger.ui.screens.base.BaseFragment
import com.example.orangemessenger.ui.screens.main_list.MainListFragment
import com.example.orangemessenger.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_add_contacts.*
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.fragment_settings.*

class CreateGroupFragment(private var listContacts:List<CommonModel>):BaseFragment(R.layout.fragment_create_group) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private var mUri = Uri.EMPTY

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        create_group_photo_civ.setOnClickListener { addPhoto() }
        create_group_btn_complete.setOnClickListener {
            val groupName = create_group_input_name.text.toString()
            if(groupName.isEmpty()){
                showToast("Введите название группы")
            } else {

                createGroupInDatabase(groupName,mUri,listContacts){
                    replaceFragment(MainListFragment())
                }

            }//else
        }
        create_group_input_name.requestFocus()
        create_groups_count_tv.text = getPlurals(listContacts.size)
    }//onResume

    private fun addPhoto() {

        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(250,250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)

    }//addPhoto

    private fun initRecyclerView() {

        mRecyclerView = create_group_recycler_view
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter
        listContacts.forEach { mAdapter.updateListItems(it) }

    }//initRecyclerView

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null) {
            mUri = CropImage.getActivityResult(data).uri
            create_group_photo_civ.setImageURI(mUri)
        }//if
    }//onActivityResult

}//CreateGroupFragment