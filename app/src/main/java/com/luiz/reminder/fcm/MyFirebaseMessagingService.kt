package com.luiz.reminder.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.luiz.reminder.R
import com.luiz.reminder.ui.activities.main.MainActivity
import com.luiz.reminder.util.Utils
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var pendingIntent: PendingIntent? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Utils.setApiFCMToken(token)
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data: Map<String?, String?> = remoteMessage.getData()
        sendNotification(data)
    }

    /**
     * Create and show a custom notification containing the received FCM message.
     *
     * @param data FCM data payload received.
     */
    private fun sendNotification(data: Map<String?, String?>) {
        val icon =
            BitmapFactory.decodeResource(resources, R.drawable.ic_icon)
        Log.d("_res", "Results notification: $data")

        if (Utils.getApiToken() != null) {
            val main = Intent(this, MainActivity::class.java)
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            pendingIntent = PendingIntent.getActivity(this, 0, main, PendingIntent.FLAG_ONE_SHOT)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(data["title"])
            .setContentText(data["body"])
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setContentInfo(data["title"])
            .setLargeIcon(icon)
            .setColor(Color.WHITE)
            .setLights(Color.WHITE, 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.ic_icon)

        try {
            val picture_url = data["picture_url"]
            if (picture_url != null && "" != picture_url) {
                val url = URL(picture_url)
                val bigPicture =
                    BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bigPicture)
                        .setSummaryText(data["body"])
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "channel description"
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.WHITE
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}