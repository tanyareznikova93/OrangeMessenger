package com.example.orangemessenger.ui.message_recycler_view.views

data class ViewVoiceMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String,
    override val text: String = ""
) :MessageView {
    override fun getTypeView(): Int {
        return MessageView.MESSAGE_VOICE
    }//getTypeView

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }

}//ViewVoiceMessage