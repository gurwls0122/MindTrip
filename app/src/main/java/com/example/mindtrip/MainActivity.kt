package com.example.mindtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.mindtrip.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var count:Int = 0
    val textarr = arrayListOf<String>("Home", "Map", "Phone", "Diary")
    val iconarr = arrayListOf<Int>(
        R.drawable.ic_baseline_home_24,
        R.drawable.ic_baseline_map_24,
        R.drawable.ic_baseline_phone_24,
        R.drawable.ic_baseline_book_24
    )

    override fun onStart() {
        super.onStart()
        if(count==0) {
            val dialog = CustomDialog(this)
            dialog.showDialog()
            count++
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.viewpager.adapter = MyViewPagerAdapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.text = textarr[position]
            tab.setIcon(iconarr[position])
        }.attach()

    }
}