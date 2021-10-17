package com.example.orangemessenger.models

// Общая модель для всех сущностей приложения
data class CommonModel(
    val id:String = "",
    var username:String = "",
    var bio:String = "",
    var fullname:String = "",
    var state:String = "",
    var phone:String = "",
    var photoUrl:String = "empty",

    val text:String = "",
    var type:String = "",
    val from:String = "",
    val timeStamp:Any = "",
    val fileUrl:String = "empty",

    var lastMessage:String = "",
    var choice:Boolean = false
){
    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == id
    }
}