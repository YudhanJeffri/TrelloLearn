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
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mukesh.tinydb.TinyDB
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.CreateTaskActivity
import java.util.*

class ReminderBroadcast : BroadcastReceiver() {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    override fun onReceive(context: Context, intent: Intent) {
        val intentRepeat = Intent(context, RepeatBroadcast::class.java)
        val pendingIntentRepeat = PendingIntent.getBroadcast(context, 0, intentRepeat, 0)
        val intent = Intent(context, CreateTaskActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val actionYes: NotificationCompat.Action = NotificationCompat.Action.Builder(0, "YA", pendingIntent).build()
        val actionNo: NotificationCompat.Action = NotificationCompat.Action.Builder(0, "BELUM", pendingIntentRepeat).build()
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "notifeuy")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentText("Apakah anda sudah Nikah?")
                .setContentTitle("Personal App")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(actionYes)
                .addAction(actionNo)
        val notification: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notification.notify(200, builder.build())

        val tinyDB = TinyDB(context)
        val userID = mFirebaseAuth.currentUser!!.uid
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val date = "$day/${month + 1}/$year"

        val docRef = mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(userID).document(tinyDB.getString("idsnapshot"))
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val y = document.getString("tanggal_masadepan")
                        val gethari = document.getString("hari_masadepan")
                        val getbulan = document.getString("bulan_masadepan")
                        val gettahun = document.getString("tahun_masadepan")

                        Log.d("tanggal_masadepan", y.toString())
                        Log.d("tanggal_sekarang", date)
                        if (gethari != null && getbulan != null) {
                            if (gethari <= day.toString() || getbulan <= month.toString()){
                                val cl = Calendar.getInstance()
                                val yearl = cl.get(Calendar.YEAR)
                                val dayl = cl.get(Calendar.DAY_OF_MONTH)
                                val monthly = cl.get(Calendar.MONTH)
                                val dately = "${dayl+1}/${monthly}/$yearl"

                                val hari = "${day + 1}"
                                val bulan = "${month + 1}"
                                val tahun = "$year"
                                val task = hashMapOf("tanggal_masadepan" to dately, "hari_masadepan" to hari, "bulan_masadepan" to bulan,"tahun_masadepan" to tahun)

                                //Toast.makeText(this, newmonth.toString(), Toast.LENGTH_LONG).show()
                                mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(userID).document(tinyDB.getString("idsnapshot"))
                                        .update(task as Map<String, Any>)
                                        .addOnSuccessListener { documentreference ->
                                            Toast.makeText(context, "Tanggal berubah!", Toast.LENGTH_LONG).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "gagal merubah tanggal", Toast.LENGTH_LONG).show()
                                        }
                                val intent = Intent()
                                intent.putExtra("samaaaaaaaaaa", 1)
                                Log.d("tanggal_sekarang", "tanggal sama (true)")
                            } else {
                                val intent = Intent()
                                intent.putExtra("samaaaaaaaaaa", 0)
                                Log.d("tanggal_sekarang", "tanggal tidak sama (falsse)")
                            }
                        }
                    } else {
                        Log.e("error", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("errorbro", "get failed with ", exception)
                }
    }
}