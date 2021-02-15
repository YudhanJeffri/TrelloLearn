package com.yudhanproject.trelloclone.activities

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mukesh.tinydb.TinyDB
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.service.ReminderBroadcast
import kotlinx.android.synthetic.main.activity_create_board.*
import java.text.DateFormat
import java.util.*


class CreateTaskActivity : AppCompatActivity() {
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var mFirebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)
        createNotificationChannel()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseFirestore = FirebaseFirestore.getInstance()

        val tinyDB = TinyDB(applicationContext)
        btnCreateTask.setOnClickListener {

            val edTask: String = createEdittext.text?.trim().toString()
            Log.d("getid", tinyDB.getString("getId"))
            if (tinyDB.getString("getId") != null) {
                addTaskToFirestore(edTask)
            }
        }
    }

    private fun updateTask(edTask: String) {
        val userID = mFirebaseAuth.currentUser!!.uid
        val tinyDB = TinyDB(applicationContext)
        val task = hashMapOf("title" to edTask)
        mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(
            userID
        ).document(tinyDB.getString("getId"))
                .update(task as Map<String, Any>)
                .addOnSuccessListener { documentreference ->
                    Toast.makeText(this, "Successful!", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to Add", Toast.LENGTH_LONG).show()
                    finish()
                }
    }

    private fun addTaskToFirestore(edTask: String) {
        val userID = mFirebaseAuth.currentUser!!.uid
        val tinyDB = TinyDB(applicationContext)
        Log.d("hehe", "ufwaf")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val date = "${day + 1}/${month + 1}/$year"
        val task = hashMapOf("title" to edTask, "tanggal_masadepan" to date)
        //Toast.makeText(this, newmonth.toString(), Toast.LENGTH_LONG).show()
        mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(
            userID
        )
                .add(task as Map<String, Any>)
                .addOnSuccessListener { documentreference ->
                    Toast.makeText(this, "Successful!", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to Add", Toast.LENGTH_LONG).show()
                    finish()
                }
        tenDetik()
    }
    fun tenDetik(){
        Toast.makeText(this, "Reminder Set", Toast.LENGTH_LONG).show()
        val alarmmanager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val month = calendar[Calendar.MONTH]
        calendar[Calendar.MONTH] = month + 3
        alarmmanager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 0, pendingIntent)
    }


    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "PersonalApp"
            val des = "ini adalah deskripsi"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifeuy", name, importance)
            channel.description = des
            val notif = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notif.createNotificationChannel(channel)
        }
    }
}