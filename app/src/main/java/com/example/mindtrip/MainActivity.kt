package com.example.mindtrip

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindtrip.databinding.ActHomeBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActHomeBinding
    val actdata: ArrayList<ActData> = ArrayList()
    lateinit var adapter: ActAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(initData()){
            binding.clearbtn.visibility = View.VISIBLE
            binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            adapter = ActAdapter(actdata)
            adapter.itemClickListener = object : ActAdapter.OnItemClickListener {
                override fun OnItemClick(data: ActData) {
//                val intent = Intent(this@MainActivity, MapActivity::class.java)
//                startActivity(intent)

                }
            }
            binding.recyclerView.adapter = adapter
        }
        init()

    }

    private fun initData():Boolean{
        try {
            val scan1 = Scanner(openFileInput("statistics.txt"))
            readFileScan(scan1, actdata)
            return true
        }catch (e:FileNotFoundException){
            return false
        }
    }
    private fun init() {

        with(binding) {
            addbtn.setOnClickListener {
                val intent = Intent(this@MainActivity,MapActivity::class.java)
                startActivity(intent)
            }
            clearbtn.setOnClickListener {
                actdata.clear()
                binding.recyclerView.adapter?.notifyDataSetChanged()
                val file = File("/data/data/com.example.mindtrip/files/statistics.txt")
                file.delete()
                clearbtn.visibility = View.GONE

            }
        }
    }
    fun readFileScan(scan: Scanner,data:ArrayList<ActData>){
        while (scan.hasNextLine()) {
            val name = scan.nextLine()
            val type = scan.nextLine()
            data.add(ActData(name, type))

        }
    }
}
