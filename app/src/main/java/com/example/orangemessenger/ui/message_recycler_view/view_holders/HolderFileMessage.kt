package com.example.orangemessenger.ui.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.orangemessenger.database.CURRENT_UID
import com.example.orangemessenger.database.getFileFromStorage
import com.example.orangemessenger.ui.message_recycler_view.views.MessageView
import com.example.orangemessenger.utilits.WRITE_FILES
import com.example.orangemessenger.utilits.asTime
import com.example.orangemessenger.utilits.checkPermission
import com.example.orangemessenger.utilits.showToast
import kotlinx.android.synthetic.main.message_item_file.view.*
import kotlinx.android.synthetic.main.message_item_voice.view.*
import java.io.File

class HolderFileMessage(view:View):RecyclerView.ViewHolder(view), MessageHolder {

    private val blockReceivedFileMessage: ConstraintLayout = view.block_received_file_message
    private val chatReceivedFileMessageTime: TextView = view.chat_received_file_message_time

    private val blockUserFileMessage: ConstraintLayout = view.block_user_file_message
    private val chatUserFileMessageTime: TextView = view.chat_user_file_message_time

    private val chatUserFilename:TextView = view.chat_user_filename
    private val chatUserBtnDownload:ImageView = view.chat_user_btn_download
    private val chatUserProgressBar:ProgressBar = view.chat_user_progress_bar

    private val chatReceivedFilename:TextView = view.chat_received_filename
    private val chatReceivedBtnDownload:ImageView = view.chat_received_btn_download
    private val chatReceivedProgressBar:ProgressBar = view.chat_received_progress_bar

    override fun drawMessage(view: MessageView) {

        if(view.from == CURRENT_UID){

            blockReceivedFileMessage.visibility = View.GONE
            blockUserFileMessage.visibility = View.VISIBLE
            chatUserFileMessageTime.text = view.timeStamp.asTime()
            chatUserFilename.text = view.text

        } else {

            blockReceivedFileMessage.visibility = View.VISIBLE
            blockUserFileMessage.visibility = View.GONE
            chatReceivedFileMessageTime.text = view.timeStamp.asTime()
            chatReceivedFilename.text = view.text

        }//else

    }//drawMessage

    override fun onAttach(view: MessageView) {

        if(view.from == CURRENT_UID) chatUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
        else chatReceivedBtnDownload.setOnClickListener { clickToBtnFile(view) }

    }//onAttach

    private fun clickToBtnFile(view: MessageView) {

        if(view.from == CURRENT_UID){
            chatUserBtnDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedBtnDownload.visibility = View.INVISIBLE
            chatReceivedProgressBar.visibility = View.VISIBLE
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            if(checkPermission(WRITE_FILES)){
                file.createNewFile()
                getFileFromStorage(file,view.fileUrl){
                    if(view.from == CURRENT_UID){
                        chatUserBtnDownload.visibility = View.VISIBLE
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        chatReceivedBtnDownload.visibility = View.VISIBLE
                        chatReceivedProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        } catch (e:Exception){
            showToast(e.message.toString())
        }

    }//clickToBtnFile

    override fun onDetach() {
        //чтобы не было утечек памяти
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)

    }//onDetach

}//HolderFileMessage