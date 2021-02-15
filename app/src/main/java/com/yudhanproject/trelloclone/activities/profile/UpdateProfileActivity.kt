package com.yudhanproject.trelloclone.activities.profile

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mukesh.tinydb.TinyDB
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.BaseActivity
import com.yudhanproject.trelloclone.firebase.FirestoreClass
import com.yudhanproject.trelloclone.models.user.User
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_registrasi.*
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import java.util.*

class UpdateProfileActivity : BaseActivity() {
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var day_tanggal: String
    lateinit var month_tanggal: String
    lateinit var year_tanggal: String
    lateinit var documentReference: DocumentReference
    lateinit var mFirebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        updatetanggalEdittext.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                // Display Selected date in TextView

                updatetanggalEdittext.setText("$day/${month+1}/$year")
                day_tanggal = day.toString()
                month_tanggal = (month+1).toString()
                year_tanggal = year.toString()
            }, year, month, day)
            dpd.show()
        }

        btn_update_profile.setOnClickListener {
            registterUser()
        }

    }
    private fun registterUser() {
        val userID = mFirebaseAuth.currentUser!!.uid
        val birth: String = updatetanggalEdittext.text.toString().trim() { it <= ' ' }
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        if (validateForm(birth)) {
            val task = hashMapOf("birth" to birth)
            mFirebaseFirestore.collection("users").document(userID)
                    .set(task as Map<String, Any>)
                    .addOnSuccessListener { documentreference ->
                        Toast.makeText(this, "Successful!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to Add", Toast.LENGTH_LONG).show()
                        finish()
                    }
            documentReference = mFirebaseFirestore.collection("tanggalLahir").document(userID)
            val userMap: MutableMap<String, String> = HashMap()
            userMap["day"] = day_tanggal
            userMap["month"] = month_tanggal
            userMap["year"] = year_tanggal
            userMap["birth"] = birth
            documentReference.set(userMap)


        }
    }
    private fun validateForm(birth: String):Boolean {
        return when {
            TextUtils.isEmpty(birth) -> {
                showErrorSnackBar("Please enter a birthday")
                false
            }
            else -> {
                true
            }
        }
    }
}