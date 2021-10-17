package com.example.orangemessenger.utilits

import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AppVoiceRecorder {

        private val mMediaRecorder = MediaRecorder()
        private lateinit var mFile:File
        private lateinit var mMessageKey:String

        fun startRecord(messageKey:String){

            try {
                mMessageKey = messageKey
                createFileForRecord()
                prepareMediaRecorder()
                mMediaRecorder.start()
            } catch (e:Exception){
                showToast(e.message.toString())
            }

        }//startRecord

        private fun prepareMediaRecorder() {

            mMediaRecorder.apply {
                //Сбрасываем перед использованием
                reset()
                setAudioSource(MediaRecorder.AudioSource.DEFAULT)
                setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
                setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                //Где будем хранить файл
                setOutputFile(mFile.absolutePath)
                prepare()
            }

        }//prepareMediaRecorder

        private fun createFileForRecord() {

            mFile = File(APP_ACTIVITY.filesDir, mMessageKey)
            mFile.createNewFile()

        }//createFileForRecord

        fun stopRecord(onSuccess: (file:File,messageKey:String) -> Unit) {

            try {
                mMediaRecorder.stop()
                onSuccess(mFile, mMessageKey)
            } catch (e:Exception){
                showToast(e.message.toString())
                mFile.delete()
            }

        }//stopRecord

        fun releaseRecorder(){

            //Освобождаем из памяти
            try {
                mMediaRecorder.release()
            } catch (e:Exception){
                showToast(e.message.toString())
            }

        }//releaseRecorder

}//AppVoiceRecorder