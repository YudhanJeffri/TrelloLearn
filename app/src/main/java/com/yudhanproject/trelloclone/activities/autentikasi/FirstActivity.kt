package com.yudhanproject.trelloclone.activities.autentikasi

import android.content.Intent
import android.os.Bundle
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_first.*

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