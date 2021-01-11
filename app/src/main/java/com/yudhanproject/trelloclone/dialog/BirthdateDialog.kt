package com.yudhanproject.trelloclone.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.yudhanproject.trelloclone.R
import kotlinx.android.synthetic.main.layout_birthdate.*
import java.lang.ClassCastException
import java.lang.Exception


class BirthdateDialog : DialogFragment() {
   /* lateinit var listener: BirthDialoglistener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.layout_birthdate, null)
        builder.setView(view)
                .setTitle("birth")
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which ->

                })
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
                    val cobastring = cobaEdittext.text.toString()
                    listener.applytext(cobastring)
                })
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as BirthDialoglistener
        } catch (e: ClassCastException){
            e.printStackTrace()
        }
    }

    public interface BirthDialoglistener{
        fun applytext(coba: String)
    }*/
}