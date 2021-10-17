package com.example.orangemessenger.utilits

import androidx.recyclerview.widget.DiffUtil
import com.example.orangemessenger.models.CommonModel

//Позволяет преобразовывать старый список в новый без полной пересборки RecyclerView
class DiffUtilCallback(

    private val oldList:List<CommonModel>,
    private val newList:List<CommonModel>

):DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].timeStamp == newList[newItemPosition].timeStamp

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}//DiffUtilCallback