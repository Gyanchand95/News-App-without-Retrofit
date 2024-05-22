package com.mynewsapp.fcm

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mynewsapp.R
import com.mynewsapp.network.ApiConstant

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // Send token to your server for future notification targeting (optional)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle data payload
        if (remoteMessage.getData().size > 0) {
            handleDataPayload(remoteMessage.getData())
        }

        // Handle notification (foreground only)
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification())
        }
    }

    private fun handleDataPayload(data: Map<String, String>) {
        // Access data key-value pairs
        val title = data["title"]
        val message = data["message"]

        // Create a Notification (assuming you're on Android)
        val notificationBuilder = NotificationCompat.Builder(applicationContext /* Get your application context here */)
            .setSmallIcon(R.mipmap.ic_launcher)  // Replace with your icon resource id
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))  // Optional: Expandable notification

        // Optionally add actions or configure notification behavior

        // Send the notification
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ApiConstant.NOTIFICATION_ID, notificationBuilder.build())

    }

    private fun sendNotification(notification: RemoteMessage.Notification?) {
        // Use notification data to create and show a notification
        // ...
    }
}