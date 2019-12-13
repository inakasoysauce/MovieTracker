package com.example.movieapplication.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.movieapplication.MovieTracker
import com.example.movieapplication.R
import com.example.movieapplication.event_bus.Events
import com.example.movieapplication.ui.splash.SplashActivity
import com.example.movieapplication.util.NotificationEvent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URL

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val title: String
        val text: String
        val intent: Intent
        if (MovieTracker.activityList.isNotEmpty()) {
            val activity = MovieTracker.activityList.last()::class.java
            intent = Intent(this, activity)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        } else {
            val activity = SplashActivity::class.java
            intent = Intent(this, activity)
        }
        intent.putExtra("type", data["type"])
        when (data["type"]) {
            "REQUEST" -> {
                title = "Friend request!"
                text = "${data["username"]} just added you as a friend!"
            }
            "RECOMMENDATION" -> {
                title = "New recommendation!"
                text = "${data["username"]} recommended you something!"
            }
            else -> {
                title = "New friend!"
                text = "${data["username"]} accepted your request!"
            }
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(nm)
        }
        val alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val url = if (data["picture_path"] != null && data["picture_path"] != "") URL(data["picture_path"]) else null
        val picture = BitmapFactory.decodeStream(url?.openConnection()?.getInputStream())
        val noti = NotificationCompat.Builder(applicationContext, "Channel")
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.get_new_friends_icon)
            .setLargeIcon(picture)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(alarm)
            .setVibrate(longArrayOf(0, 100, 100, 100, 100))
            .setAutoCancel(true)
            .build()
        nm.notify(1, noti)
        val uid = data["uid"]
        Events.friendEvent(NotificationEvent(uid!!, data["type"]!!))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(nm: NotificationManager) {
        val name = getString(R.string.channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1", name, importance)
        nm.createNotificationChannel(channel)
    }
}