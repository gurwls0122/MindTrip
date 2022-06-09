package com.example.mindtrip

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.DatePicker
import com.example.mindtrip.databinding.ActivityDiaryEditBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class DiaryEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryEditBinding
    val cal = Calendar.getInstance()
    var titlekey = intent.getStringExtra("titlekey")
    lateinit var rdb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rdb = Firebase.database.getReference("Diary/Contents")
        initData()

        binding.calendarbtn.setOnClickListener {
            showDatePickerDialog()
        }
        //editData()
        deleteData()

        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    private fun deleteData() {
        rdb.child(titlekey.toString()).removeValue()
        finish()

    }

    /*private fun editData() {
        binding.editbtn.setOnClickListener {
            println("수정")
        }
    }*/

    private fun initData() {
        val title = titlekey
        if (title!=null){
            rdb.child(title).child("title").get().addOnSuccessListener {
                binding.diaryTitle.setText(it.value.toString())
                println("data")
                println(binding.diaryTitle.text)
            }
            rdb.child(title).child("year").get().addOnSuccessListener {
                binding.year.setText(it.value.toString())
                println("data")
                println(binding.year.text)
            }
            rdb.child(title).child("month").get().addOnSuccessListener {
                binding.month.setText(it.value.toString())
                println("data")
                println(binding.month.text)
            }
            rdb.child(title).child("day").get().addOnSuccessListener {
                binding.day.setText(it.value.toString())
                println("data")
                println(binding.day.text)
            }
            rdb.child(title).child("content").get().addOnSuccessListener {
                binding.diaryText.setText(it.value.toString())
                println("data")
                println(binding.diaryText.text)
            }
        }

    }

    private fun showDatePickerDialog(){
        var datePicker = DatePickerDialog(
            this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    binding.year.text = year.toString()
                    binding.month.text = (month+1).toString()
                    binding.day.text = dayOfMonth.toString()
                }
            },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }

    private fun frequency(): Int{
        val word: Array<String> = arrayOf("나","자신","저","나를","우울", "항상","아무것도","완전히")
        val text = binding.diaryText.text.toString()
        var count = 0

        for (i in word){
            count += countMatches(text, i)
        }
        return count

    }
    fun countMatches(string: String, pattern: String): Int {
        return string.split(pattern)
            .dropLastWhile { it.isEmpty() }
            .toTypedArray().size - 1
    }
}