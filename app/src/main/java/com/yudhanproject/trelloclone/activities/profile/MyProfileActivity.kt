 package com.yudhanproject.trelloclone.activities.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.layout_resetpassword.view.*

class MyProfileActivity : BaseActivity() {

    lateinit var mFirebaseFirestore: FirebaseFirestore
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var documentReference: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        val user = mFirebaseAuth.currentUser

        btn_gantipass.setOnClickListener {
            Toast.makeText(this,"Clicked",Toast.LENGTH_LONG).show()

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_harapan, null)
            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
                    .setView(mDialogView)
            /*mBuilder.setMessage("Enter New Password > 6 Characters Long")
            mBuilder.setPositiveButton("Yes") { _, _ ->
                val edpassword = mDialogView.editText_ganti.text.toString()
                user?.updatePassword(edpassword)?.addOnCanceledListener {
                    Toast.makeText(this, "Password Reset Successfully", Toast.LENGTH_LONG).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "Password Reset Failed", Toast.LENGTH_LONG).show()
                }*/


            /*val reseted = EditText(this)
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Reset Password?")
            alertDialogBuilder.setMessage("Enter New Password > 6 Characters Long")
            alertDialogBuilder.setView(reseted)
            alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                val edpassword = reseted.text.toString()
                user?.updatePassword(edpassword)?.addOnCanceledListener {
                    Toast.makeText(this,"Password Reset Successfully",Toast.LENGTH_LONG).show()
                }?.addOnFailureListener {
                    Toast.makeText(this,"Password Reset Failed",Toast.LENGTH_LONG).show()
                }
            })*/
        }


        // if google else firebase
        if (isSignedIn()){
            getItemGoogle()
            Toast.makeText(this,"ini google",Toast.LENGTH_LONG).show()
        } else {
        getItemFirebase()
        }

        btn_editprofile.setOnClickListener {
            startActivity(Intent(this,UpdateProfileActivity::class.java))
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