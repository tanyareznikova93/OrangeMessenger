package com.example.orangemessenger

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.orangemessenger.database.AUTH
import com.example.orangemessenger.database.initFirebase
import com.example.orangemessenger.database.initUser
import com.example.orangemessenger.databinding.ActivityMainBinding
import com.example.orangemessenger.ui.objects.AppDrawer
import com.example.orangemessenger.ui.screens.main_list.MainListFragment
import com.example.orangemessenger.ui.screens.register.EnterPhoneNumberFragment
import com.example.orangemessenger.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //Отложенная инииализация (чтобы не делать проверку на null)
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mToolbar: Toolbar
    lateinit var mAppDrawer: AppDrawer

    /*
    lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){

        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity().setAspectRatio(1,1)
                .setRequestedSize(600,600)
                .setCropShape(CropImageView.CropShape.OVAL)
                .getIntent(this@MainActivity)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }

     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser{
            //initContacts()

           CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }

            initFields()
            initFunc()
        }

    }//override fun onCreate(.)

    private fun initFunc() {

        setSupportActionBar(mToolbar)
        if(AUTH.currentUser!=null){
            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(),false)
        }

        /*
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
            it?.let{uri -> val path:StorageReference = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)
            path.putFile(uri).addOnCompleteListener{
                if(it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                }//if
            }//addOnCompleteListener
            }
        }

         */


    }//private fun initFunc()

    private fun initFields() {

        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()

    }//private fun initFields()

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }//onStart

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }//onStop

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }

}//class MainActivity