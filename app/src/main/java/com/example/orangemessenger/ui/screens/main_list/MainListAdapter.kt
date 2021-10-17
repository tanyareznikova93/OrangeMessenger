package com.example.orangemessenger.ui.screens.main_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orangemessenger.R
import com.example.orangemessenger.models.CommonModel
import com.example.orangemessenger.ui.screens.groups.GroupChatFragment
import com.example.orangemessenger.ui.screens.single_chat.SingleChatFragment
import com.example.orangemessenger.utilits.TYPE_CHAT
import com.example.orangemessenger.utilits.TYPE_GROUP
import com.example.orangemessenger.utilits.downloadAndSetImage
import com.example.orangemessenger.utilits.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_main_list.view.*
import kotlinx.android.synthetic.main.main_list_item.view.*

class MainListAdapter :RecyclerView.Adapter<MainListAdapter.MainListHolder>() {

    private var listItem = mutableListOf<CommonModel>()

    class MainListHolder(view: View) : RecyclerView.ViewHolder(view){

        val itemName:TextView = view.main_list_item_fullname_tv
        val itemLastMessage:TextView = view.main_list_item_last_message_tv
        val itemPhoto:CircleImageView = view.main_list_item_photo_civ

    }//MainListHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item,parent,false)
        val holder = MainListHolder(view)
        holder.itemView.setOnClickListener {

            when(listItem[holder.adapterPosition].type){
                TYPE_CHAT -> replaceFragment(SingleChatFragment(listItem[holder.adapterPosition]))
                TYPE_GROUP -> replaceFragment(GroupChatFragment(listItem[holder.adapterPosition]))
            }//when

        }

        return holder
    }//onCreateViewHolder

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {

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