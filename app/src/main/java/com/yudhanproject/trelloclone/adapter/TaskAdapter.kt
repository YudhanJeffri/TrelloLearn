package com.yudhanproject.trelloclone.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mukesh.tinydb.TinyDB
import com.yudhanproject.trelloclone.R
import com.yudhanproject.trelloclone.activities.UpdateTaskActivity
import com.yudhanproject.trelloclone.models.TaskModel
import kotlinx.android.synthetic.main.item_task.view.*
import java.util.*


class TaskAdapter(options: FirestoreRecyclerOptions<TaskModel>, val context: Context) : FirestoreRecyclerAdapter<TaskModel, TaskAdapter.TaskAdapterVH>(options) {

    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var mFirebaseFirestore: FirebaseFirestore


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapterVH {
        return TaskAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
    }
    override fun onBindViewHolder(holder: TaskAdapterVH, position: Int, model: TaskModel) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseFirestore = FirebaseFirestore.getInstance()

        val userID = mFirebaseAuth.currentUser!!.uid

        // get the id document of Task
        val snapshot = snapshots.getSnapshot(holder.adapterPosition)
        val tinyDB = TinyDB(context)
        val getID = snapshot.id
        holder.textTask.text = model.title
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val date = "$day/${month + 1}/$year"
            val docRef = mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(userID).document(getID)
            docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val y = document.getString("tanggal_masadepan")
                            Log.d("tanggal_masadepan", y.toString())
                            Log.d("tanggal_sekarang",date)
                            if (y == date){
                                val cl = Calendar.getInstance()
                                val yearl = cl.get(Calendar.YEAR)
                                val dayl = cl.get(Calendar.DAY_OF_MONTH)
                                val monthly = cl.get(Calendar.MONTH)
                                val dately = "$dayl/${monthly + 4}/$yearl"

                                val task = hashMapOf("tanggal_masadepan" to dately)
                                //Toast.makeText(this, newmonth.toString(), Toast.LENGTH_LONG).show()
                                mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(userID).document(getID)
                                        .update(task as Map<String, Any>)
                                        .addOnSuccessListener { documentreference ->
                                            Toast.makeText(context, "Tanggal berubah!", Toast.LENGTH_LONG).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "gagal merubah tanggal", Toast.LENGTH_LONG).show()
                                        }
                                val intent = Intent()
                                intent.putExtra("samaaaaaaaaaa",1)
                                Log.d("tanggal_sekarang","tanggal sama (true)")
                            } else {
                                val intent = Intent()
                                intent.putExtra("samaaaaaaaaaa",0)
                                Log.d("tanggal_sekarang","tanggal tidak sama (falsse)")
                            }
                        } else {
                            Log.e("error", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("errorbro", "get failed with ", exception)
                    }

        holder.deleteTask.setOnClickListener {
            mFirebaseFirestore.collection(userID).document(tinyDB.getInt("getUmur").toString()).collection(userID).document(getID)
                    .delete()
                    .addOnSuccessListener { documentreference ->
                        Toast.makeText(context, "Successful Deleted!", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to Add", Toast.LENGTH_LONG).show()
                    }
            Toast.makeText(context,"icon update clicked $getID",Toast.LENGTH_LONG).show()
        }
        holder.updateTask.setOnClickListener {
            // put at string, send to createactivity
            tinyDB.putString("getId",getID)
            context.startActivity(Intent(context,UpdateTaskActivity::class.java))
        }
    }
    class TaskAdapterVH(itemview: View) : RecyclerView.ViewHolder(itemview){
        var textTask = itemview.text_itemTask
        var updateTask = itemview.update_icon
        var deleteTask = itemview.delete_icon
    }

}