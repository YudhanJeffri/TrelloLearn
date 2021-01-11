package com.yudhanproject.trelloclone.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.firebase.FirestoreClass
import kotlinx.android.synthetic.main.activity_first.*
import org.joda.time.LocalDate
import org.joda.time.Years
import java.lang.Exception
import java.util.*

class FirstActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        /*val currentUserID = FirestoreClass().getCurrentUserID()
        if (currentUserID.isNotEmpty()){
            startActivity(Intent(this, MainActivity::class.java))
        }*/

        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        daftar.setOnClickListener {
            startActivity(Intent(this, RegistrasiActivity::class.java))
        }
    }
}