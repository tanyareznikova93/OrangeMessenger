package com.example.orangemessenger.ui.screens.main_list

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.orangemessenger.R
import com.example.orangemessenger.database.*
import com.example.orangemessenger.models.CommonModel
import com.example.orangemessenger.utilits.*
import kotlinx.android.synthetic.main.fragment_main_list.*

//Главный фрагмент, содержит все чаты, группы и каналы с которыми взаимодействует пользователь
class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MainListAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "OrangeMessenger"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()
    }//onResume

    private fun initRecyclerView() {

        mRecyclerView = main_list_recycler_view
        mAdapter = MainListAdapter()

        //1 запрос
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener{dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach {model ->

                when(model.type){

                    TYPE_CHAT -> showChat(model)
                    TYPE_GROUP -> showGroup(model)

                }//when

            }
        })

        mRecyclerView.adapter = mAdapter

    }//initRecyclerView

    private fun showGroup(model: CommonModel) {

        //2 запрос
        REF_DATABASE_ROOT.child(NODE_GROUPS).child(model.id)
            .addListenerForSingleValueEvent(AppValueEventListener{ dataSnapshot1 ->
            val newModel = dataSnapshot1.getCommonModel()

            //3 запрос
                REF_DATABASE_ROOT.child(NODE_GROUPS).child(model.id).child(NODE_MESSAGES)
                    .limitToLast(1)
                    .addListenerForSingleValueEvent(AppValueEventListener{ dataSnapshot2 ->
                val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                if(tempList.isEmpty()){
                    newModel.lastMessage = "Чат очищен"
                } else {
                    newModel.lastMessage = tempList[0].text
                }

                newModel.type = TYPE_GROUP
                mAdapter.updateListItems(newModel)
            })
        })

    }//showGroup

    private fun showChat(model: CommonModel) {

        //2 запрос
        mRefUsers.child(model.id)
            .addListenerForSingleValueEvent(AppValueEventListener{dataSnapshot1 ->
            val newModel = dataSnapshot1.getCommonModel()

            //3 запрос
            mRefMessages.child(model.id)
                .limitToLast(1)
                .addListenerForSingleValueEvent(AppValueEventListener{dataSnapshot2 ->
                val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                if(tempList.isEmpty()){
                    newModel.lastMessage = "Чат очищен"
                } else newModel.lastMessage = tempList[0].text

                if(newModel.fullname.isEmpty()){
                    newModel.fullname = newModel.phone
                }//if

                newModel.type = TYPE_CHAT
                mAdapter.updateListItems(newModel)
            })
        })

    }//showChat

}//MainListFragment