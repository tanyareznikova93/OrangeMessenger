package com.example.orangemessenger.ui.message_recycler_view.views

import com.example.orangemessenger.models.CommonModel
import com.example.orangemessenger.utilits.TYPE_MESSAGE_FILE
import com.example.orangemessenger.utilits.TYPE_MESSAGE_IMAGE
import com.example.orangemessenger.utilits.TYPE_MESSAGE_VOICE

class AppViewFactory {

    companion object{
        fun getView(message:CommonModel):MessageView{

            return when(message.type){
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                TYPE_MESSAGE_VOICE -> ViewVoiceMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                TYPE_MESSAGE_FILE -> ViewFileMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
            }

        }//getView
    }

}//AppViewFactory