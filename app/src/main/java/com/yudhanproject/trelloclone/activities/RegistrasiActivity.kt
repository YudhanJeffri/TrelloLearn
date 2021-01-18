    package com.yudhanproject.trelloclone.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.firebase.FirestoreClass
import com.yudhanproject.trelloclone.models.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registrasi.*
import java.util.*


    class RegistrasiActivity : BaseActivity() {

        companion object {
            private const val RC_SIGN_IN = 1
        }

    lateinit var db: FirebaseFirestore
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var day_tanggal: String
    lateinit var month_tanggal: String
    lateinit var year_tanggal: String
    lateinit var documentReference: DocumentReference
    lateinit var mFirebaseFirestore: FirebaseFirestore
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth





        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            auth = FirebaseAuth.getInstance()
            google_btn_registrasi.setOnClickListener {
                signIn()
            }

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

        private fun signIn() {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val exception = task.exception
                if (task.isSuccessful) {
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)
                        Log.d("SignActivity", "firebaseAuthWithGoogle:" + account!!.id)
                        if (account.idToken != null){
                            firebaseAuthWithGoogle(account.idToken!!)
                        } else {
                            Log.d("gagal","id token null")
                        }
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        Log.w("SignActivity", "Google sign in failed", e)
                        // ...
                    }
                } else {
                    Log.w("SignActivity", exception.toString())
                }
            }
        }

        private fun firebaseAuthWithGoogle(idToken: String) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignActivity", "signInWithCredential:success")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignActivity", "signInWithCredential:failure", task.exception)
                        }
                    }
        }

        private fun registterUser(){

        val name: String = nameEdittext.text.toString().trim(){it<= ' '}
        val email: String = emailEdittext.text.toString().trim(){it<= ' '}
        val birth: String = birthDate.text.toString().trim(){it<= ' '}
        val password: String = passwordEdittext.text.toString().trim(){it<= ' '}
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseFirestore = FirebaseFirestore.getInstance()
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
                val userID = mFirebaseAuth.currentUser!!.uid
                documentReference = mFirebaseFirestore.collection("tanggalLahir").document(userID)
                val userMap: MutableMap<String, String> = HashMap()
                userMap["day"] = day_tanggal
                userMap["month"] = month_tanggal
                userMap["year"] = year_tanggal
                userMap["birth"] = birth
                documentReference.set(userMap)


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
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a username")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter a email")
                false
            }
            TextUtils.isEmpty(birth) -> {
                showErrorSnackBar("Please enter a birthday")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> {
                true
            }
        }

    }
}