package com.example.mindtrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mindtrip.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var homeFragment =HomeFragment()
    private var mapFragment = MapFragment()
    private var phoneFragment = PhoneFragment()
    private var diaryFragment =DiaryFragment()
    private var count:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavi()
    }

    override fun onStart() {
        super.onStart()
        if(count==0) {
            val dialog = CustomDialog(this)
            dialog.showDialog()
            count++
        }
    }

    private fun initNavi() {
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.fragment_container, homeFragment, "home").commit()

        binding.mainNavi.run{
            setOnItemSelectedListener {
                when(it.itemId){
                    R.id.Home -> {
                        val frag = supportFragmentManager.beginTransaction()
                        supportFragmentManager.popBackStack("home",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        frag.replace(R.id.fragment_container, homeFragment, "home").addToBackStack("home").commit()
                    }
                    R.id.Phone -> {
                        val frag = supportFragmentManager.beginTransaction()
                        supportFragmentManager.popBackStack("phone",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        frag.replace(R.id.fragment_container, phoneFragment, "phone").addToBackStack("phone").commit()
                    }
                    R.id.Map -> {
                        val frag = supportFragmentManager.beginTransaction()
                        supportFragmentManager.popBackStack("map",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        frag.replace(R.id.fragment_container, mapFragment, "map").addToBackStack("map").commit()
                    }
                    R.id.Diary -> {
                        val frag = supportFragmentManager.beginTransaction()
                        supportFragmentManager.popBackStack("diary",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        frag.replace(R.id.fragment_container, diaryFragment, "diary").addToBackStack("diary").commit()
                    }

                }
                true
            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val bnv = findViewById<View>(R.id.main_navi) as BottomNavigationView
        updateBottomMenu(bnv)
    }

    private fun updateBottomMenu(navigation:BottomNavigationView){
        val tag1:Fragment? = supportFragmentManager.findFragmentByTag("home")
        val tag2:Fragment? = supportFragmentManager.findFragmentByTag("phone")
        val tag3:Fragment? = supportFragmentManager.findFragmentByTag("map")
        val tag4:Fragment? = supportFragmentManager.findFragmentByTag("diary")

        if(tag1 != null && tag1.isVisible) {navigation.menu.findItem(R.id.Home).isChecked= true }
        if(tag2 != null && tag2.isVisible) {navigation.menu.findItem(R.id.Phone).isChecked = true }
        if(tag3 != null && tag3.isVisible) {navigation.menu.findItem(R.id.Map).isChecked = true }
        if(tag4 != null && tag4.isVisible) {navigation.menu.findItem(R.id.Diary).isChecked = true }
    }
}

