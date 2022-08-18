package com.udacity.ui

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.R
import com.udacity.custombtn.ButtonState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.udacity.util.sendNotification



class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url: String = GLIDE_URL
    private var repoName : String = "Glide"
    private var downloadStatus : String? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        //call function to create the notification channel
        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

        custom_button.setOnClickListener {
            custom_button.buttonState = ButtonState.Loading
            download()
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioGlide -> {
                   url =  GLIDE_URL
                   repoName = "Glide"
                }
                R.id.radioLoadApp -> {
                    url = UDACITY_URL
                    repoName = "LoadApp"

                }
                R.id.radioRetrofit -> {
                    url = RETROFIT_URL
                    repoName = "Retrofit"

                }
            }

        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //Set the button state back to 'completed'
            custom_button.buttonState = ButtonState.Completed

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                //Check status of download
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val query = DownloadManager.Query()
                query.setFilterById(id)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {

                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    //Log successful download and set user readable message text
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloadStatus = "Successfully downloaded!"
                        notification()
                        Toast.makeText(applicationContext,downloadStatus,Toast.LENGTH_LONG).show()

                    }else if (status == DownloadManager.STATUS_FAILED) {
                        downloadStatus = "Failed, Please try again."
                       // errorToast()
                        Toast.makeText(applicationContext,downloadStatus,Toast.LENGTH_LONG).show()

                    }



                }
            }


        }
    }


    fun notification(){
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(
            repoName,
            downloadStatus!!, downloadID,
            downloadStatus!!, applicationContext)
    }


    //Create the notification channel for download notifications
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.enableVibration(true)
        notificationChannel.description =
            applicationContext.getString(R.string.notification_channel_description)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)


        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }

    companion object {
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/bumptech/glide"
        private const val CHANNEL_ID = "LoadMeUp"
        private const val TAG = "MainActivity"
    }

}
