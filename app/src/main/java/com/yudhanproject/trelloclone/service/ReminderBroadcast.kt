package com.yudhanproject.trelloclone.service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.CreateTaskActivity

class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intent = Intent(context, CreateTaskActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val actionYes: NotificationCompat.Action = NotificationCompat.Action.Builder(0,"YA",pendingIntent).build()
        val actionNo: NotificationCompat.Action = NotificationCompat.Action.Builder(0,"BELUM",pendingIntent).build()
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context,"notifeuy")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentText("Apakah anda sudah Nikah?")
                .setContentTitle("Personal App")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(actionYes)
                .addAction(actionNo)
        val notification: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notification.notify(200,builder.build())
    }
}