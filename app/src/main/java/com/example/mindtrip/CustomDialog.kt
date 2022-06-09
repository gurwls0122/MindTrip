package com.example.mindtrip

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.security.SecureRandom

class CustomDialog(context: Context) {
    private val dialog = Dialog(context)
    lateinit var eng:TextView
    lateinit var kor:TextView
    lateinit var person:TextView
    lateinit var quote_str:String
    lateinit var str:String
    fun showDialog()
    {
        val rdb = Firebase.database.reference
        val secureRandom = SecureRandom()
        quote_str = "Quote" + (secureRandom.nextInt(5)+1)
        Log.i("random", quote_str)
        rdb.child("Quote").child(quote_str)
            .child("Eng").get().addOnSuccessListener {
                eng = dialog.findViewById(R.id.Eng_Quote)
                str = "\"" + it.value + "\""
                eng.text = str
        }
        rdb.child("Quote").child(quote_str)
            .child("Kor").get().addOnSuccessListener {
                kor = dialog.findViewById(R.id.Kor_Quote)
                str = "\"" + it.value + "\""
                kor.text = str
            }
        rdb.child("Quote").child(quote_str)
            .child("person").get().addOnSuccessListener {
                person = dialog.findViewById(R.id.person)
                str = " - " + it.value
                person.text = str
            }
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        dialog.findViewById<Button>(R.id.dialog_btn).setOnClickListener {
            dialog.dismiss()
        }
    }
}