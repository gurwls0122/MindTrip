package com.example.mindtrip

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.DatePicker
import com.example.mindtrip.databinding.ActivityDiaryWriteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class DiaryWriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryWriteBinding
    val cal = Calendar.getInstance()
    lateinit var rdb: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //날짜 선택
        binding.calendarbtn.setOnClickListener {
            showDatePickerDialog()
        }

        //저장
        binding.savebtn.setOnClickListener {
            val frequency = frequency()
            rdb = Firebase.database.getReference("Diary/Contents")

            binding.apply {
                val item = DiaryData(year.text.toString().toInt(),month.text.toString().toInt(),
                    day.text.toString().toInt(), diaryTitle.text.toString(), diaryText.text.toString(), frequency)
                rdb.child(diaryTitle.text.toString()).setValue(item)
            }

            //println try
            /*val title = binding.diaryTitle.text.toString()
            rdb.child(title).child("title").get().addOnSuccessListener {
                binding.savebtn.setText(it.value.toString())
            }*/
            finish()
        }

        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    //datepicker
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
        val word: Array<String> = arrayOf("나","나는","나를","나의","자신","저","저는","제가","우울", "항상","아무것도","완전히","영원히","전혀")
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