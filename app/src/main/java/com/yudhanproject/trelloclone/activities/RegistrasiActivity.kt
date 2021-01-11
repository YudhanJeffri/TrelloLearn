    package com.yudhanproject.trelloclone.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.firebase.FirestoreClass
import com.yudhanproject.trelloclone.models.User
import kotlinx.android.synthetic.main.activity_registrasi.*
import java.util.*


    class RegistrasiActivity : BaseActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var day_tanggal: String
    lateinit var month_tanggal: String
    lateinit var year_tanggal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        birthDate.setOnClickListener {
                val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    // Display Selected date in TextView

                    birthDate.setText("$day/${month+1}/$year")
                    day_tanggal = day.toString()
                    month_tanggal = (month+1).toString()
                    year_tanggal = year.toString()
                }, year, month, day)
                dpd.show()
        }
        signUp.setOnClickListener {
            registterUser()
        }



    }
    private fun registterUser(){
        val name: String = nameEdittext.text.toString().trim(){it<= ' '}
        val email: String = emailEdittext.text.toString().trim(){it<= ' '}
        val birth: String = birthDate.text.toString().trim(){it<= ' '}
        val password: String = passwordEdittext.text.toString().trim(){it<= ' '}
        mFirebaseAuth = FirebaseAuth.getInstance()
        val userID = mFirebaseAuth.currentUser?.uid


        if (userID != null) {
            db.collection("users").document(userID)
                    .set(day_tanggal)
            db.collection("users").document(userID)
                    .set(month_tanggal)
            db.collection("users").document(userID)
                    .set(year_tanggal)
            db.collection("users").document(userID)
                    .set(birth)
        }


        if (validateForm(name, email, birth, password)){
            Toast.makeText(this, "now we can registere a new user", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val registeredEmail = firebaseUser.email!!
                            val user = User(firebaseUser.uid, name, registeredEmail, birth, day_tanggal,month_tanggal,year_tanggal)
                            FirestoreClass().registerUser(this, user)
                        } else {
                            Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                        }
                    }


        }
    }



    fun userRegisteredSuccess(){
        Toast.makeText(
                this, "you have succesfully registered the email address", Toast.LENGTH_LONG
        ).show()
        FirebaseAuth.getInstance().signOut()
        finish()

    }

    private fun validateForm(name: String, email: String, birth: String, password: String):Boolean{
        return when {
            TextUtils.isEmpty(name)-> {
                showErrorSnackBar("Please enter a username")
                false
            }
            TextUtils.isEmpty(email)-> {
                showErrorSnackBar("Please enter a email")
                false
            }
            TextUtils.isEmpty(birth)-> {
                showErrorSnackBar("Please enter a birthday")
                false
            }
            TextUtils.isEmpty(password)-> {
                showErrorSnackBar("Please enter a password")
                false
            } else -> {
                true
        }
    }



   /* private fun registeruser() {
        val username = usernameEdittext.editableText.toString().trim()
        val email = emailEdittext.editableText.toString().trim()
        val date = birthDate.editableText.toString().trim()
        val password = passwordEdittext.editableText.toString().trim()

        if (date.length in 4..4){
            birthDate.error = "Masukkan Tahun lahir anda"
            birthDate.requestFocus()
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdittext.error = "Masukkan Email"
            emailEdittext.requestFocus()
        }*/


    }
}