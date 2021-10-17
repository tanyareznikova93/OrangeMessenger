package com.example.orangemessenger.ui.screens.groups

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.orangemessenger.ui.message_recycler_view.view_holders.*
import com.example.orangemessenger.ui.message_recycler_view.views.MessageView

class GroupChatAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListMessagesCache = mutableListOf<MessageView>()
    private var mListHolders = mutableListOf<MessageHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return AppHolderFactory.getHolder(parent,viewType)

    }//onCreateViewHolder

    override fun getItemViewType(position: Int): Int {
        return mListMessagesCache[position].getTypeView()
    }//getItemViewType

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as MessageHolder).drawMessage(mListMessagesCache[position])

    }//onBindViewHolder

    //появляется на экране
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {

        (holder as MessageHolder).onAttach(mListMessagesCache[holder.adapterPosition])
        mListHolders.add((holder as MessageHolder))
        super.onViewAttachedToWindow(holder)

    }//onViewAttachedToWindow

    //уходит с экрана
    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {

        (holder as MessageHolder).onDetach()
        mListHolders.remove((holder as MessageHolder))
        super.onViewDetachedFromWindow(holder)

    }//onViewDetachedFromWindow

    fun addItemToBottom(item:MessageView, onSuccess:() -> Unit){
        if(!mListMessagesCache.contains(item)){
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSuccess()
    }//addItemToBottom

    fun addItemToTop(item:MessageView, onSuccess:() -> Unit){
        if(!mListMessagesCache.contains(item)){
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }//addItemToTop

    fun onDestroy() {

        mListHolders.forEach{
            it.onDetach()
        }

    }//onDestroy

}//GroupChatAdapter