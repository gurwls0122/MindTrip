package com.example.mindtrip

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.example.dmindtrip.databinding.ActivityDiaryWriteBinding
import java.util.*
import kotlin.collections.ArrayList

class DiaryWriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryWriteBinding
    val cal = Calendar.getInstance()
    val newdata:ArrayList<DiaryData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calendarbtn.setOnClickListener {
            showDatePickerDialog()
        }

        binding.savebtn.setOnClickListener {
            val frequency = frequency()
            binding.apply {
                newdata.add(DiaryData(year.text.toString(),month.text.toString(),
                day.text.toString(), diaryTitle.text.toString(), diaryText.text.toString(), frequency))
            }
            println("data")
            println(newdata)
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
        val word: Array<String> = arrayOf("나","자신","나를","우울", "항상","아무것도","완전히")
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
