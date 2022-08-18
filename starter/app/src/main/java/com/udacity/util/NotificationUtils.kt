package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.ui.DetailActivity

//Create extension function to send the notifications. Additional parameters used
//to provide extra information tin Intent call
fun NotificationManager.sendNotification(
    fileName: String,
    status: String,
    id: Long,
    messageBody: String,
    applicationContext: Context
) {
    //Create the Intent to open Details Activity from notification
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    //Add the extra information to the Intent request, for display on the Details Activity
    contentIntent.putExtra(applicationContext.getString(R.string.download_request), fileName)
    contentIntent.putExtra(applicationContext.getString(R.string.download_status), status)
    contentIntent.putExtra(applicationContext.getString(R.string.id), id)
    val detailPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        id.toInt(),
        contentIntent,
        0
    )
    //Construct the notification
    val notificationBuilder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        //Add action to navigate to Detail Activity
        .addAction(
            R.drawable.download,
            applicationContext.getString(R.string.notification_button),
            detailPendingIntent
        )

    notify(id.toInt(), notificationBuilder.build())
}

//Create extension function to cancel the specific notification (by Id)
fun NotificationManager.cancelNotification(id: Long){
    cancel(id.toInt())
}