package com.example.orangemessenger.utilits

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.orangemessenger.MainActivity
import com.example.orangemessenger.R
import com.example.orangemessenger.database.updatePhonesToDatabase
import com.example.orangemessenger.models.CommonModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.*
import java.text.SimpleDateFormat
import java.util.*

//Файл для хранения утилитарных функции, доступных во всем приложении

//Функция показывает сообщение
fun showToast(message:String){
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

//Функция расширения для AppCompatActivity, позволяет запускать активити
fun restartActivity(){
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()

}//restartActivity

//Функция расширения для AppCompatActivity, позволяет устанавливать фрагменты
fun replaceFragment(fragment: Fragment, addStack:Boolean = true){
    if(addStack){
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.data_container, fragment).commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.data_container, fragment).commit()
    }

}//replaceFragment

//Функция скрывает клавиатуру
fun hideKeyboard() {
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken,0)
}//hideKeyboard

//Функция раширения ImageView, скачивает и устанавливает картинку
fun ImageView.downloadAndSetImage(url:String){
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.default_pic1)
        .into(this)
}

//Функция считывает контакты с телефонной книги, хаполняет массив arrayContacts моделями CommonModel
fun initContacts() {

    if(checkPermission(READ_CONTACTS)){
        val arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        //Читаем телефонную книгу пока есть следующие элементы
        cursor?.let {
            while (it.moveToNext()){
                val fullname = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullname
                newModel.phone = phone.replace(Regex("[\\s,-]"),"")
                arrayContacts.add(newModel)
            }//while
        }//cursor?.let
        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }//if

}//initContacts

//Получаем время отправки сообщения
fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}//String.asTime()

//Получаем название файла по Uri
fun getFilenameFromUri(uri: Uri): String {

    var result = ""
    val cursor = APP_ACTIVITY.contentResolver.query(uri,null,null,null,null)

    try {
        if(cursor != null && cursor.moveToFirst()){
            result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    } catch (e:Exception){
        showToast(e.message.toString())
    } finally {
        cursor?.close()
        return result
    }

}//getFilenameFromUri

//Получаем Plurals
fun getPlurals(count:Int) = APP_ACTIVITY.resources.getQuantityString(
    R.plurals.count_members,count,count
)