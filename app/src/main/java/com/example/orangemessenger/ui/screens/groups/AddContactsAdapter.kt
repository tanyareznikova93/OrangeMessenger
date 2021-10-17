package com.example.orangemessenger.ui.screens.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orangemessenger.R
import com.example.orangemessenger.models.CommonModel
import com.example.orangemessenger.ui.screens.single_chat.SingleChatFragment
import com.example.orangemessenger.utilits.downloadAndSetImage
import com.example.orangemessenger.utilits.replaceFragment
import com.example.orangemessenger.utilits.showToast
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.add_contacts_item.view.*
import kotlinx.android.synthetic.main.fragment_main_list.view.*
import kotlinx.android.synthetic.main.main_list_item.view.*
import kotlinx.android.synthetic.main.main_list_item.view.main_list_item_last_message_tv

class AddContactsAdapter :RecyclerView.Adapter<AddContactsAdapter.AddContactsHolder>() {

    private var listItem = mutableListOf<CommonModel>()

    class AddContactsHolder(view: View) : RecyclerView.ViewHolder(view){

        val itemName:TextView = view.add_contacts_item_fullname_tv
        val itemLastMessage:TextView = view.add_contacts_item_last_message_tv
        val itemPhoto:CircleImageView = view.add_contacts_item_photo_civ
        val itemChoice:CircleImageView = view.add_contacts_item_choice_civ

    }//MainListHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactsHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_contacts_item,parent,false)
        val holder = AddContactsHolder(view)
        holder.itemView.setOnClickListener {
            if(listItem[holder.adapterPosition].choice) {
                holder.itemChoice.visibility = View.INVISIBLE
                listItem[holder.adapterPosition].choice = false
                AddContactsFragment.listContacts.remove(listItem[holder.adapterPosition])
            } else {
                holder.itemChoice.visibility = View.VISIBLE
                listItem[holder.adapterPosition].choice = true
                AddContactsFragment.listContacts.add(listItem[holder.adapterPosition])
            }//else
        }

        return holder
    }//onCreateViewHolder

    override fun onBindViewHolder(holder: AddContactsHolder, position: Int) {

        holder.itemName.text = listItem[position].fullname
        holder.itemLastMessage.text = listItem[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItem[position].photoUrl)

    }//onBindViewHolder

    override fun getItemCount(): Int = listItem.size

    fun updateListItems(item:CommonModel){

        listItem.add(item)
        notifyItemInserted(listItem.size)

    }//updateListItems

}//MainListAdapter