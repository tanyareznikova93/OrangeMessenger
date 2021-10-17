package com.example.orangemessenger.utilits

import android.media.MediaPlayer
import com.example.orangemessenger.database.REF_DATABASE_ROOT
import com.example.orangemessenger.database.REF_STORAGE_ROOT
import com.example.orangemessenger.database.getFileFromStorage
import java.io.File

class AppVoicePlayer {

    private lateinit var mMediaPlayer:MediaPlayer
    private lateinit var mFile:File

    fun play(messageKey:String,fileUrl:String,function: () -> Unit) {
        mFile = File(APP_ACTIVITY.filesDir,messageKey)
        if(mFile.exists() && mFile.length()>0 && mFile.isFile){
            startPlay{
                //получаем callback
                function()
            }
        } else {
            mFile.createNewFile()
            getFileFromStorage(mFile,fileUrl){
                startPlay{
                    //получаем callback
                    function()
                }
            }
        }//else

    }//play

    private fun startPlay(function: () -> Unit) {

        try {

            mMediaPlayer.setDataSource(mFile.absolutePath)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener {
                stop{
                    //возвращаем callback
                    function()
                }
            }

        } catch(e:Exception){
            showToast(e.message.toString())
        }

    }//startPlay

    fun stop(function: () -> Unit) {

        try {

            mMediaPlayer.stop()
            //сбрасываем плеер
            mMediaPlayer.reset()
            //возвращаем callback
            function()

        } catch(e:Exception){
            showToast(e.message.toString())
            //возвращаем callback
            function()
        }

    }//stop

    fun release(){

        mMediaPlayer.release()

    }//release

    fun init(){

        mMediaPlayer = MediaPlayer()

    }//init

}//AppVoicePlayer