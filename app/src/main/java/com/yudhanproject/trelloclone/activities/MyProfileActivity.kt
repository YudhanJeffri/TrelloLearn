package com.yudhanproject.trelloclone.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.Document
import com.squareup.okhttp.internal.DiskLruCache
import com.yudhanproject.trelloclone.R
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : BaseActivity() {

    lateinit var mFirebaseFirestore: FirebaseFirestore
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var documentReference: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()


        // if google else firebase
        if (isSignedIn()){
            getItemGoogle()
            Toast.makeText(this,"ini google",Toast.LENGTH_LONG).show()
        } else {
        getItemFirebase()
        }

    }
    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }
    fun getItemGoogle(){
        mFirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = mFirebaseAuth.currentUser
        nama.text = currentUser?.displayName
        email.text = currentUser?.email

        val userID = mFirebaseAuth.currentUser!!.uid
        val docRef = mFirebaseFirestore.collection("users").document(userID)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        tanggal_lahir.text = document.getString("birth")
                    } else {
                        Log.e("error", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("errorbro", "get failed with ", exception)
                }
    }

    fun getItemFirebase() {
        val userID = mFirebaseAuth.currentUser!!.uid
        Log.e("iniUID", userID)
        val docRef = mFirebaseFirestore.collection("users").document(userID)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        nama.text = document.getString("name")
                        email.text = document.getString("email")
                        tanggal_lahir.text = document.getString("birth")
                    } else {
                        Log.e("error", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("errorbro", "get failed with ", exception)
                }
    }
}