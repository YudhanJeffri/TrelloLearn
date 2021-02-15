package com.yudhanproject.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mukesh.tinydb.TinyDB
import com.yudhanproject.trelloclone.R
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.activity_update.*

class UpdateTaskActivity : AppCompatActivity() {
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var mFirebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        val tinyDB = TinyDB(applicationContext)
        Toast.makeText(this,tinyDB.getString("getId"), Toast.LENGTH_LONG).show()
        btnUpdateTask.setOnClickListener {
            val edTask:String = updateEdittext.text?.trim().toString()
            Log.d("getid", tinyDB.getString("getId"))
            if (tinyDB.getString("getId") != null) {
                updateTask(edTask)
            }
        }
    }
    private fun updateTask(edTask: String){
        val userID = mFirebaseAuth.currentUser!!.uid
        val tinyDB = TinyDB(applicationContext)
        val task = hashMapOf("title" to edTask)
        mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(userID).document(tinyDB.getString("getId"))
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
}