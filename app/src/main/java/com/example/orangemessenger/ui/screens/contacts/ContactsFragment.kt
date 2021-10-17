package com.example.orangemessenger.ui.screens.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orangemessenger.R
import com.example.orangemessenger.database.*
import com.example.orangemessenger.models.CommonModel
import com.example.orangemessenger.ui.screens.base.BaseFragment
import com.example.orangemessenger.ui.screens.single_chat.SingleChatFragment
import com.example.orangemessenger.utilits.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.contact_item.view.*
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter:FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts:DatabaseReference
    private lateinit var mRefUsers:DatabaseReference
    private lateinit var mRefUsersListener:AppValueEventListener
    private var mapListeners = HashMap<DatabaseReference,AppValueEventListener>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
        initRecyclerView()
    }//onResume

    private fun initRecyclerView() {
        mRecyclerView = contacts_recycler_view
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        //Настройка для адаптера, где указываем какие данные и откуда получать
        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts,CommonModel::class.java)
            .build()

        //Адаптер принимает данные, отображает в холдере
        mAdapter = object :FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options){

            ////Запускается тогда когда адаптер получает доступ к ViewGroup
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item,parent,false)
                return ContactsHolder(view)
            }//onCreateViewHolder

            //Заполняет holder
            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()

                    if(contact.fullname.isEmpty()){
                        holder.name.text = model.fullname
                    } else holder.name.text = contact.fullname

                    holder.state.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener { replaceFragment(SingleChatFragment(model)) }
                }

                mRefUsers.addValueEventListener(mRefUsersListener)
                mapListeners[mRefUsers] = mRefUsersListener

            }//onBindViewHolder
        }//object :FirebaseRecyclerAdapter
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }//initRecyclerView

    //Холдер для захвата ViewGroup
    class ContactsHolder(view:View):RecyclerView.ViewHolder(view){
        val name:TextView = view.contact_fullname_tv
        val state:TextView = view.contact_state_tv
        val photo:CircleImageView = view.contact_photo_civ
    }//class ContactsHolder

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListeners.forEach{
            it.key.removeEventListener(it.value)
        }
    }//onPause
}
