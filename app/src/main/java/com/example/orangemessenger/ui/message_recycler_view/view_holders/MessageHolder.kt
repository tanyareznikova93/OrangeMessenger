package com.example.orangemessenger.ui.message_recycler_view.view_holders

import com.example.orangemessenger.ui.message_recycler_view.views.MessageView

interface MessageHolder {

    fun drawMessage(view:MessageView)
    //когда holder появился на экране
    fun onAttach(view:MessageView)
    //будем отключать собия кликов
    fun onDetach()

}//MessageInterface