package com.example.orangemessenger.ui.screens.groups

import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.orangemessenger.R
import com.example.orangemessenger.database.*
import com.example.orangemessenger.models.CommonModel
import com.example.orangemessenger.models.User
import com.example.orangemessenger.ui.screens.base.BaseFragment
import com.example.orangemessenger.ui.message_recycler_view.views.AppViewFactory
import com.example.orangemessenger.ui.screens.main_list.MainListFragment
import com.example.orangemessenger.utilits.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.choice_upload.*
import kotlinx.android.synthetic.main.fragment_group_chat.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.fragment_single_chat.chat_input_message_et
import kotlinx.android.synthetic.main.toolbar_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupChatFragment(private val group: CommonModel) : BaseFragment(R.layout.fragment_group_chat) {

    private lateinit var mListenerInfoToolbar:AppValueEventListener
    private lateinit var mReceivingUser:User
    private lateinit var mToolbarInfo:View
    private lateinit var mRefUser:DatabaseReference
    private lateinit var mRefMessages:DatabaseReference
    private lateinit var mAdapter:GroupChatAdapter
    private lateinit var mRecyclerView: RecyclerView
    //Чтобы избежать утечек памяти создаем mMessageListener
    private lateinit var mMessageListener: AppChildEventListener
    private var mCountMessages = 10
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>

    override fun onResume() {
        super.onResume()
        initFields()
        initToolbar()
        initRecyclerView()
    }//onResume

    private fun initFields() {

        setHasOptionsMenu(true)
        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_choice)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        mAppVoiceRecorder = AppVoiceRecorder()
        mSwipeRefreshLayout = group_chat_swipe_refresh
        mLayoutManager = LinearLayoutManager(this.context)

        group_chat_input_message_et.addTextChangedListener(AppTextWatcher{
            val string = group_chat_input_message_et.text.toString()
            if(string.isEmpty() || string == "Запись"){
                group_chat_btn_send_message_iv.visibility = View.GONE
                group_chat_btn_attach_iv.visibility = View.VISIBLE
                group_chat_btn_voice_iv.visibility = View.VISIBLE
            } else {
                group_chat_btn_send_message_iv.visibility = View.VISIBLE
                group_chat_btn_attach_iv.visibility = View.GONE
                group_chat_btn_voice_iv.visibility = View.GONE
            }
        })

        group_chat_btn_attach_iv.setOnClickListener { attach() }

        CoroutineScope(Dispatchers.IO).launch {

            group_chat_btn_voice_iv.setOnTouchListener { v, event ->
                if(checkPermission(RECORD_AUDIO)){
                    if(event.action == MotionEvent.ACTION_DOWN){
                        //TODO record
                        group_chat_input_message_et.setText("Запись")
                        group_chat_btn_voice_iv.setColorFilter(ContextCompat.getColor(APP_ACTIVITY,R.color.md_blue_400))
                        val messageKey = getMessageKey(group.id)
                        mAppVoiceRecorder.startRecord(messageKey)
                    } else if(event.action == MotionEvent.ACTION_UP) {
                        //TODO stop record
                        group_chat_input_message_et.setText("")
                        group_chat_btn_voice_iv.setColorFilter(null)
                        mAppVoiceRecorder.stopRecord(){file,messageKey ->
                            uploadFileToStorageForGroup(
                                Uri.fromFile(file),
                                messageKey,
                                group.id,
                                TYPE_MESSAGE_VOICE
                            )
                            mSmoothScrollToPosition = true
                        }
                    }//else if
                }//if
                true
            }

        }//CoroutineScope

    }//initFields

    private fun attach() {

        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        btn_attach_file.setOnClickListener { attachFile() }
        btn_attach_image.setOnClickListener { attachImage() }

    }//attach

    private fun attachFile(){

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)


    }//attachFile

    private fun attachImage() {

        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(250,250)
            .start(APP_ACTIVITY, this)

    }//attachFile

    private fun initRecyclerView() {

        mRecyclerView = group_chat_recycler_view
        mAdapter = GroupChatAdapter()

        mRefMessages = REF_DATABASE_ROOT
            .child(NODE_GROUPS)
            .child(group.id)
            .child(NODE_MESSAGES)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.layoutManager = mLayoutManager

        mMessageListener = AppChildEventListener{
            val message = it.getCommonModel()
            if(mSmoothScrollToPosition){
                mAdapter.addItemToBottom(AppViewFactory.getView(message)){
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(AppViewFactory.getView(message)){
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }

        }

        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessageListener)


        mRecyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(mIsScrolling && dy<0 && mLayoutManager.findFirstVisibleItemPosition()<=3){
                    updateData()
                }//if
            }//onScrolled

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    mIsScrolling = true
                }//if
            }//onScrollStateChanged

        })

        mSwipeRefreshLayout.setOnRefreshListener { updateData() }

    }//initRecyclerView

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefMessages.removeEventListener(mMessageListener)
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessageListener)
    }//updateData

    private fun initToolbar() {
        mToolbarInfo = APP_ACTIVITY.mToolbar.toolbar_info
        mToolbarInfo.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }
        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(group.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)

        group_chat_btn_send_message_iv.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = group_chat_input_message_et.text.toString()
            if (message.isEmpty()) {
                showToast("Введите сообщение")
            } else sendMessageToGroup(message, group.id, TYPE_TEXT) {
                group_chat_input_message_et.setText("")
            }
        }
    }//initToolbar

    private fun initInfoToolbar() {
        if(mReceivingUser.fullname.isEmpty()){
            mToolbarInfo.toolbar_chat_fullname_tv.text = group.fullname
        } else mToolbarInfo.toolbar_chat_fullname_tv.text = mReceivingUser.fullname
        mToolbarInfo.toolbar_chat_image.downloadAndSetImage(mReceivingUser.photoUrl)
        mToolbarInfo.toolbar_chat_state_tv.text = mReceivingUser.state
    }//initInfoToolbar

    //Активность которая запускается для получения картинки для фото пользователя
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null){
            when(requestCode){
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val uri = CropImage.getActivityResult(data).uri
                    val messageKey = getMessageKey(group.id)
                    uploadFileToStorageForGroup(uri, messageKey, group.id, TYPE_MESSAGE_IMAGE)
                    mSmoothScrollToPosition = true
                }
                PICK_FILE_REQUEST_CODE -> {
                    val uri = data.data
                    val messageKey = getMessageKey(group.id)
                    val filename = getFilenameFromUri(uri!!)
                    uploadFileToStorageForGroup(uri,messageKey,group.id, TYPE_MESSAGE_FILE,filename)
                    mSmoothScrollToPosition = true
                }

            }//when
        }//if

    }//onActivityResult

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
        mRefMessages.removeEventListener(mMessageListener)
    }//onPause

    override fun onDestroy() {
        super.onDestroy()
        mAppVoiceRecorder.releaseRecorder()
        mAdapter.onDestroy()
    }//onDestroy

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.group_chat_action_menu, menu)
    }//onCreateOptionsMenu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.group_menu_clear_chat -> clearChatInGroup(group.id){
                showToast("Чат очищен")
                replaceFragment(MainListFragment())
            }
            R.id.group_menu_delete_chat -> deleteChat(group.id){
                showToast("Чат удален")
                replaceFragment(MainListFragment())
            }
        }
        return true
    }//onOptionsItemSelected

}//SingleChatFragment