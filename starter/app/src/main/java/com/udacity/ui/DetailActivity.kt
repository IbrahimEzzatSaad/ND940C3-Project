package com.udacity.ui

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.R
import com.udacity.util.cancelNotification
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        //Retrieve information passed with Intent
        val filename =
            intent.getStringExtra(applicationContext.getString(R.string.download_request))
        val status = intent.getStringExtra(applicationContext.getString(R.string.download_status))
        val id = intent.getLongExtra(applicationContext.getString(R.string.id), 0)

        //Cancel the notification (specific, not all notifications)
        cancelNotification(id)

        //Set values of retrieved information to TextViews
        filename_textview.text = filename
        status_textview.text = status

        //Show status as GREEN if success, or RED if failed
        setStatusColor(status)

        //Exit Detail Activity
        button_return.setOnClickListener {
            finish()
        }
    }

    //Function cancels the specific notification based on Id
    private fun cancelNotification(id: Long) {
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotification(id)
    }

    //Status is shown in green for success, red for failed
    private fun setStatusColor(status: String?) {
        if (status == "Successfully downloaded!") {
            status_textview.setTextColor(Color.GREEN)
        } else {
            status_textview.setTextColor(Color.RED)
        }
    }
}
