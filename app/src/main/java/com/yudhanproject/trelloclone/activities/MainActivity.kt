package com.yudhanproject.trelloclone.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.adapter.BoardAdapter
import com.yudhanproject.trelloclone.models.Board
import com.yudhanproject.trelloclone.models.BoardHighlighted
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_registrasi.*
import kotlinx.android.synthetic.main.layout_birthdate.view.*
import kotlinx.android.synthetic.main.layout_birthdate.view.cobaEdittext
import kotlinx.android.synthetic.main.layout_harapan.*
import kotlinx.android.synthetic.main.layout_harapan.view.*
import org.joda.time.LocalDate
import org.joda.time.Years
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


class MainActivity : AppCompatActivity(){
    lateinit var mFirebaseFirestore: FirebaseFirestore
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var documentReference: DocumentReference
    lateinit var day_birth: String
    lateinit var month_birth: String
    lateinit var year_birth: String
    lateinit var birth: String
    lateinit var harapanHidup: String
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var boardAdapter: BoardAdapter
    lateinit var getHarapanHidup: String

    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var calendar: Calendar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        birth = usiaMain.text.toString()
        harapanHidup = harapanText.text.toString()
        Log.e("waduh", ""+harapanHidup)
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        val userID = mFirebaseAuth.currentUser!!.uid
        if (harapanHidup == "harapan" || harapanHidup == " " || harapanHidup == "null" || harapanHidup == "") {
            getHarapanHidup()
        }


        if (isSignedIn()){
            Toast.makeText(this, "kamu pake google", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "pake firebase", Toast.LENGTH_LONG).show()
        }
        fab_create_board.setOnClickListener {
            startActivity(Intent(this, CreateBoardActivity::class.java))
        }

    }


    fun getHarapanHidup(){
        val userID = mFirebaseAuth.currentUser!!.uid
        val docRef = mFirebaseFirestore.collection("harapanHidup").document(userID)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                                harapanText.text = document.getString("harapan")

                            harapanHidup = harapanText.text.toString()

                            Toast.makeText(this, "ternyata : $harapanHidup",Toast.LENGTH_LONG).show()
                            if (harapanHidup == "harapan" || harapanHidup == " " || harapanHidup == "null" || harapanHidup == ""){
                                harapanHidup()
                                Toast.makeText(this, "ternyata : $harapanHidup",Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this,"no such document",Toast.LENGTH_LONG).show()
                        }

                        if (isSignedIn()) {
                            googleBirthdate()
                        } else {
                            firebaseBirthdate()
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.e("errorbro", "get failed with ", exception)
                    }
    }
    private fun openDialogBirthdate(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_birthdate, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
        mDialogView.cobaEdittext.inputType = InputType.TYPE_NULL
        val mAlertDialog = mBuilder.show()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        mDialogView.cobaEdittext.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, day ->
                // Display Selected date in TextView

                umurMain.setText("$day/${month + 1}/$year")
                mDialogView.cobaEdittext.setText("$day/${month + 1}/$year")
                day_birth = day.toString()
                month_birth = (month + 1).toString()
                year_birth = year.toString()
            }, year, month, day)
            dpd.show()
        }
        mAlertDialog.setCancelable(false)

        mDialogView.btn_lanjut.setOnClickListener {
            mAlertDialog.dismiss()

            val birth: String = mDialogView.cobaEdittext.text.toString().trim(){it<= ' '}
            mFirebaseAuth = FirebaseAuth.getInstance()
            val userID = mFirebaseAuth.currentUser!!.uid
            documentReference = mFirebaseFirestore.collection("users").document(userID)
            val userMap: MutableMap<String, String> = HashMap()
            userMap["day"] = day_birth
            userMap["month"] = month_birth
            userMap["year"] = year_birth
            userMap["birth"] = birth

            val birthdate = LocalDate(year_birth.toInt(), month_birth.toInt(), day_birth.toInt())
            val now = LocalDate()
            val age = Years.yearsBetween(birthdate, now)
            val umurku = age.toString()
            val a = umurku.replace(Regex("""[P,Y]"""), "")
            usiaMain.text = "umur : $a"
            documentReference.set(userMap).addOnSuccessListener {
                Toast.makeText(this,"success added",Toast.LENGTH_LONG).show()
            }
            val harapantext: String = harapanText.text.toString().trim(){it<= ' '}
            if (harapantext == "null"){
                harapanHidup()
            } else {
                Toast.makeText(this,"harapan udah ada",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun harapanHidup(){
        mFirebaseAuth = FirebaseAuth.getInstance()
        val userID = mFirebaseAuth.currentUser!!.uid
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_harapan, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.setCancelable(false)
        val hh = mDialogView.findViewById<EditText>(R.id.harapanEdittext)
        hh.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {

                val c = Runnable {
                    val harapan:String = mDialogView.harapanEdittext.text.toString().trim(){it<= ' '}
                    mFirebaseAuth = FirebaseAuth.getInstance()
                    documentReference = mFirebaseFirestore.collection("harapanHidup").document(userID)
                    val userMap: MutableMap<String, String> = HashMap()
                    userMap["harapan"] = harapan
                    documentReference.set(userMap)
                }
                val hand = Handler()
                hand.postDelayed(c,1)
            }
        })

        mDialogView.btn_selesai.setOnClickListener {
            val docRef = mFirebaseFirestore.collection("harapanHidup").document(userID)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            harapanText.text = document.getString("harapan")
                            getHarapanHidup =  harapanText.text.toString()
                        } else {
                            Log.e("error", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("errorbro", "get failed with ", exception)
                    }
            mAlertDialog.dismiss()
            /*startActivity(Intent(this,MainActivity::class.java))
            finish()*/
        }

    }

    private fun googleBirthdate(){
        if (harapanHidup == "harapan" || harapanHidup == " " || harapanHidup == "null" || harapanHidup == ""){
            harapanHidup()
            Toast.makeText(this, "ternyata aaaaa: $harapanHidup",Toast.LENGTH_LONG).show()
        } else {
            val userID = mFirebaseAuth.currentUser!!.uid

            val docRef = mFirebaseFirestore.collection("users").document(userID)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            day_birth = document.getString("day").toString()
                            month_birth = document.getString("month").toString()
                            year_birth = document.getString("year").toString()


                            if (day_birth == "null" && month_birth == "null" && year_birth == "null") {
                                openDialogBirthdate()
                            } else {
                                day_birth = document.getString("day").toString()
                                month_birth = document.getString("month").toString()
                                year_birth = document.getString("year").toString()
                                umurMain.text = "$day_birth/$month_birth/$year_birth"
                                val birthdate = LocalDate(year_birth.toInt(), month_birth.toInt(), day_birth.toInt())
                                val now = LocalDate()
                                val age = Years.yearsBetween(birthdate, now)
                                val umurku = age.toString()
                                val a = umurku.replace(Regex("""[P,Y]"""), "")
                                usiaMain.text = "umur : $a"
                                val exlist = generateDummyList(a.toInt())
                                val highlightedlist = generateHighlighted(a.toInt())
                                rv_board.adapter = BoardAdapter(exlist, highlightedlist, a)
                                rv_board.layoutManager = LinearLayoutManager(this)
                                rv_board.setHasFixedSize(true)
                            }
                        }
                    }
        }
    }

    private fun firebaseBirthdate(){
        if (harapanHidup == "harapan" || harapanHidup == " " || harapanHidup == "null" || harapanHidup == ""){
            harapanHidup()
            Toast.makeText(this, "ternyata aaaaa: $harapanHidup",Toast.LENGTH_LONG).show()
        } else {
            val userID = mFirebaseAuth.currentUser!!.uid
            Log.e("iniUID", userID)
            val docRef = mFirebaseFirestore.collection("users").document(userID)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            day_birth = document.getString("day").toString()
                            month_birth = document.getString("month").toString()
                            year_birth = document.getString("year").toString()

                            umurMain.text = "$day_birth/$month_birth/$year_birth"
                            val birthdate = LocalDate(year_birth.toInt(), month_birth.toInt(), day_birth.toInt())
                            val now = LocalDate()
                            val age = Years.yearsBetween(birthdate, now)
                            val umurku = age.toString()
                            val a = umurku.replace(Regex("""[P,Y]"""), "")
                            usiaMain.text = "umur : $a"
                            val exlist = generateDummyList(a.toInt())
                            val highlightedlist = generateHighlighted(a.toInt())
                            rv_board.adapter = BoardAdapter(exlist, highlightedlist, a)
                            rv_board.layoutManager = LinearLayoutManager(this)
                            rv_board.setHasFixedSize(true)
                        } else {
                            Log.e("error", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("errorbro", "get failed with ", exception)
                    }
        }
    }


    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }
    private fun generateHighlighted(size: Int): List<BoardHighlighted> {
        val list = ArrayList<BoardHighlighted>()
        getHarapanHidup =  harapanText.text.toString()
        val value = Integer.valueOf(harapanHidup)
        for (i in 1 until value) {
            val drawable = when (i % 1) {
                0 -> R.drawable.ic_baseline_add_24
                else -> R.drawable.common_full_open_on_phone
            }
            val item = BoardHighlighted(drawable, "Umur $i")
            list += item
        }
        return list
    }

    private fun generateDummyList(size: Int): List<Board> {
        val list = ArrayList<Board>()
        getHarapanHidup =  harapanText.text.toString()

        val value = Integer.valueOf(getHarapanHidup)
        Log.e("error", getHarapanHidup)
        for (i in 1 until value) {
            val drawable = when (i % 1) {
                0 -> R.drawable.ic_launcher_foreground
                else -> R.drawable.common_full_open_on_phone
            }
            val item = Board(drawable, "Umur $i")
            list += item
        }
        return list
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected  (item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_signout -> {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                mGoogleSignInClient.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.menu_myprofile -> {
                startActivity(Intent(this, MyProfileActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}