package com.yudhanproject.trelloclone.firebase

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.yudhanproject.trelloclone.activities.LoginActivity
import com.yudhanproject.trelloclone.activities.MainActivity
import com.yudhanproject.trelloclone.activities.RegistrasiActivity
import com.yudhanproject.trelloclone.models.User
import com.yudhanproject.trelloclone.models.UserBirth
import com.yudhanproject.trelloclone.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegistrasiActivity, userInfo: User){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener {
            Log.e(activity.javaClass.simpleName, "Error writing document")
            }
    }
    fun registerUserBirth(activity: MainActivity, userInfo: UserBirth){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {
                }.addOnFailureListener {
                    Log.e(activity.javaClass.simpleName, "Error writing document")
                }
    }
    fun getCurrentUserID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }



    fun signInUser(activity: LoginActivity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null)
                activity.signInSuccess(loggedInUser)
            }.addOnFailureListener {
                Log.e("SignInUser", "Error writing document")
            }
    }

}