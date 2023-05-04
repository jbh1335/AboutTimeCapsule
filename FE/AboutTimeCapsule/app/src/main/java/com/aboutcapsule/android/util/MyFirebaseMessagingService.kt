package com.aboutcapsule.android.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService :FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    companion object {
        private const val CHANNEL_NAME = "AboutTimeCapsule"
        private const val CHANNEL_DESCRIPTION = "AboutTimeCapsule를 위한 채널"
        private const val CHANNEL_ID = "채널 ID"
    }
}