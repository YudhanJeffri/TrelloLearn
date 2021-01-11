package com.yudhanproject.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.yudhanproject.trelloclone.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
    fun getCurrentUserID(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
    fun showErrorSnackBar(msg:String){
        val snackbar = Snackbar.make(findViewById(android.R.id.content),
        msg, Snackbar.LENGTH_LONG)
        val snackbarview = snackbar.view
        snackbarview.setBackgroundColor(ContextCompat.getColor(this,R.color.snackbar_error_color))
        snackbar.show()
    }
}