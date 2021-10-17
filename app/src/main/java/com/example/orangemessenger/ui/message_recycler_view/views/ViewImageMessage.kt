package com.example.orangemessenger.ui.message_recycler_view.views

data class ViewImageMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String,
    override val text: String = ""
) :MessageView {
    override fun getTypeView(): Int {
        return MessageView.MESSAGE_IMAGE
    }//getTypeView

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }

}//ViewImageMessage