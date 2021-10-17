package com.example.orangemessenger.ui.screens.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.*
import com.example.orangemessenger.R
import com.example.orangemessenger.database.*
import com.example.orangemessenger.ui.screens.base.BaseFragment
import com.example.orangemessenger.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*

//Фрагмент настроек
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_bio_tv.text = USER.bio
        settings_full_name_tv.text = USER.fullname
        settings_phone_number_tv.text = USER.phone
        settings_status_tv.text = USER.state
        settings_user_name_tv.text = USER.username
        settings_btn_change_user_name.setOnClickListener{ replaceFragment(ChangeUsernameFragment()) }
        settings_btn_change_bio.setOnClickListener{ replaceFragment(ChangeBioFragment()) }
        settings_change_photo_civ.setOnClickListener{ changeUserPhoto() }
        settings_user_photo_civ.downloadAndSetImage(USER.photoUrl)

    }//initFields

    // Изменения фото пользователя
    private fun changeUserPhoto() {

        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(250,250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)

    }//changeUserPhoto

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && requestCode== RESULT_OK && data!=null) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            putFileToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        settings_user_photo_civ.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                    }//putUrlToDatabase
                }//getUrlFromStorage
            }//putFileToStorage
        }
            /*
            path.putFile(uri).addOnCompleteListener{
                if(it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                }
            }

             */
        }

    }//onActivityResult

 */

    // Создания выпадающего меню
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }//onCreateOptionsMenu

    //Слушатель выбора пунктов выпадающего меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }//onOptionsItemSelected

    /*
    val activityResultLauncher = registerForActivityResult(ActivityResultContracts
    .StartActivityForResult(requestCode: Int, resultCode: Int, data: Intent?))
    {
        result ->
        if (result.resultCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && result.resultCode == Activity.RESULT_OK && result.data != null
        ) {
            val uri = CropImage.getActivityResult(result.data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            putFileToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        settings_user_photo_civ.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                    }//putUrlToDatabase
                }//getUrlFromStorage
            }//putFileToStorage
        }//if
    }

     */

    //Активность которая запускается для получения картинки для фото пользователя
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            putFileToStorage(uri, path){
                getUrlFromStorage(path){
                    putUrlToDatabase(it){
                        settings_user_photo_civ.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }//putUrlToDatabase
                }//getUrlFromStorage
            }//putFileToStorage
        }//if
    }//onActivityResult

}//SettingsFragment