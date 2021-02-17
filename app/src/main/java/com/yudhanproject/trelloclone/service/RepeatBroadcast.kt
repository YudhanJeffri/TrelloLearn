package com.yudhanproject.trelloclone.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mukesh.tinydb.TinyDB
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.CreateTaskActivity
import java.util.*

class RepeatBroadcast : BroadcastReceiver() {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onReceive(context: Context, intent: Intent) {
        NotificationManagerCompat.from(context).cancel(200)
        val intentRepeat = Intent(context, ReminderBroadcast::class.java)
        val pendingIntentRepeat = PendingIntent.getBroadcast(context, 0, intentRepeat, 0)
        val alarmmanager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val month = calendar[Calendar.MONTH]
        val second = calendar[Calendar.SECOND]
        val day = calendar[Calendar.DAY_OF_MONTH]
        calendar[Calendar.DAY_OF_MONTH] = day + 1
        alarmmanager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 0, pendingIntentRepeat)

        val userID = mFirebaseAuth.currentUser!!.uid
        val tinyDB = TinyDB(context)
        Log.d("hehe", "ufwaf")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val dayl = c.get(Calendar.DAY_OF_MONTH)
        val monthl = c.get(Calendar.MONTH)
        val date = "${dayl + 1}/${monthl + 1}/$year"
        val hari = "${day + 1}"
        val bulan = "${month + 1}"
        val tahun = "$year"
        val task = hashMapOf("tanggal_masadepan" to date, "hari_masadepan" to hari, "bulan_masadepan" to bulan,"tahun_masadepan" to tahun)

        //Toast.makeText(this, newmonth.toString(), Toast.LENGTH_LONG).show()
        mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(userID).document(tinyDB.getString("idsnapshot"))
            .update(task as Map<String, Any>)
            .addOnSuccessListener { documentreference ->
                Toast.makeText(context, "Tanggal berubah!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "gagal merubah tanggal", Toast.LENGTH_LONG).show()
            }
    }
}