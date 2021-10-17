package com.example.orangemessenger.ui.screens.register

import androidx.fragment.app.Fragment
import com.example.orangemessenger.R
import com.example.orangemessenger.database.AUTH
import com.example.orangemessenger.utilits.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import java.util.concurrent.TimeUnit

//Фрагмент для ввода номера телефона при регистрации
class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber:String
    private lateinit var mCallBack:PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        //Callback который возвращает результат верификации
        mCallBack = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            /* Функция срабатывает если верификация уже была произведена,
                * пользователь авторизируется в приложении без потверждения по смс */
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                AUTH.signInWithCredential(credential).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        showToast("Добро пожаловать")
                        restartActivity()
                    } else showToast(task.exception?.message.toString())
                }

            }//onVerificationCompleted

            //Функция срабатывает если верификация не удалась
            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }//onVerificationFailed

            //Функция срабатывает если верификация впервые, и отправлена смс
            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber,id))
            }//onCodeSent

        }
        register_btn_next.setOnClickListener{sendCode()}
    }//onStart

    /* Функция проверяет поле для ввода номер телефона, если поле пустое выводит сообщение.
         * Если поле не пустое, то начинает процедуру авторизации/ регистрации */
    private fun sendCode() {

        if(register_input_phone_number_et.text.toString().isEmpty()){
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            authUser()
        }

    }//sendCode

    //Инициализация
    private fun authUser() {

        mPhoneNumber = register_input_phone_number_et.text.toString()
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions
                .newBuilder(FirebaseAuth.getInstance())
                .setActivity(APP_ACTIVITY)
                .setPhoneNumber(mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallBack)
                .build()
        )

        /*mPhoneNumber = register_input_phone_number_et.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            activity as RegisterActivity,
            mCallBack
        )*/
    }//authUser

}//EnterPhoneNumberFragment