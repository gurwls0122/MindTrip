package com.example.mindtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dmindtrip.databinding.ActivityDiaryWriteBinding
import java.util.*
import kotlin.collections.ArrayList

class DiaryEditActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryWriteBinding
    val cal = Calendar.getInstance()
    val editdata:ArrayList<DiaryData> = ArrayList()
    var position = intent.getStringExtra("position")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()

        binding.savebtn.setOnClickListener {
            binding.apply {
                val beforedata = editdata[position!!.toInt()]
                val afterdata = beforedata.copy(year.text.toString(),month.text.toString(),
                    day.text.toString(), diaryTitle.text.toString(), diaryText.text.toString())
            }
            println("data")
            println(editdata)
            finish()
        }

        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    private fun initData() {
        if (position != null) {
            var data = editdata[position!!.toInt()]
            binding.year.text = data.year
            binding.month.text = data.month
            binding.day.text = data.day
        }
    }
}
