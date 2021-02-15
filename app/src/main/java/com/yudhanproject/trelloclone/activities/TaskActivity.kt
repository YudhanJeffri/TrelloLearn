package com.yudhanproject.trelloclone.activities

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mukesh.tinydb.TinyDB
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.adapter.TaskAdapter
import com.yudhanproject.trelloclone.models.TaskModel
import com.yudhanproject.trelloclone.service.ReminderBroadcast
import kotlinx.android.synthetic.main.activity_task.*
import java.util.*


class TaskActivity : AppCompatActivity() {

    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var mFirebaseFirestore: FirebaseFirestore
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var collectionReference: CollectionReference
    var taskAdapter: TaskAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        mFirebaseAuth = FirebaseAuth.getInstance()
        val userID = mFirebaseAuth.currentUser!!.uid
        collectionReference = db.collection(userID)
        setUpRV()
        val tinyDB = TinyDB(applicationContext)
        if (intent.getIntExtra("samaaaaaaaaaa",0) == 1){
           Toast.makeText(this,"sama",Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this,"tidak sama",Toast.LENGTH_LONG).show()
        }

        val intent = intent
        val gText = intent.getStringExtra("gText")
        val gPosition = intent.getIntExtra("gPosition",0)

        text_dummy.text = gText
        text_position.text = gPosition.toString()
        mFirebaseFirestore = FirebaseFirestore.getInstance()

        val marks = arrayOf(mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(userID))
        for(item in marks){
            Log.d("iniarraynya", item.toString())
        }



        fab_create_board.setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }
    }

    private fun setUpRV() {
        val tinyDB = TinyDB(applicationContext)
        val userID = mFirebaseAuth.currentUser!!.uid
        Toast.makeText(this, tinyDB.getString("getId"), Toast.LENGTH_LONG).show()
        val query: Query = collectionReference.document(tinyDB.getInt("getUmur").toString())
                .collection(userID)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<TaskModel> = FirestoreRecyclerOptions.Builder<TaskModel>()
                .setQuery(query, TaskModel::class.java)
                .build()
        taskAdapter = TaskAdapter(firestoreRecyclerOptions,this)
        rv_task .layoutManager = LinearLayoutManager(this)
        rv_task.adapter = taskAdapter

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PersonalApp"
            val des = "ini adalah deskripsi"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifeuy", name, importance)
            channel.description = des
            val notif = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notif.createNotificationChannel(channel)
        }
    }

    override fun onStart() {
        super.onStart()
        taskAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        taskAdapter!!.stopListening()
    }

    fun button(view: View) {
        Toast.makeText(this, "Reminder Set", Toast.LENGTH_LONG).show()
        val alarmmanager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val year = calendar[Calendar.YEAR]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val second = calendar[Calendar.SECOND]
        calendar[Calendar.SECOND] = second + 10
        alarmmanager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 0, pendingIntent)
    }
}
