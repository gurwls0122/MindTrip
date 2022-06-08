package com.example.mindtrip

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableRow
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
        initRecyclerView()
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

    private fun initRecyclerView(){
        actdata.clear()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        if(initData()){
            binding.clearbtn.visibility = View.VISIBLE
            binding.refreshbtn.visibility = View.VISIBLE
            binding.refreshbtn2.visibility = View.GONE
            binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            adapter = ActAdapter(actdata)
            adapter.itemClickListener = object : ActAdapter.OnItemClickListener {
                override fun OnItemClick(data: ActData) {

                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder!!.setMessage("Google Maps에 볼까요?")
                        .setTitle(data.name)

                    builder.apply {
                        setPositiveButton("예") { dialog, id ->
                            val selectedId = id
                            val gmmIntentUri = Uri.parse("geo:0,0?q=${data.name}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            startActivity(mapIntent)

                        }
                        setNegativeButton("아니요") { dialog, id ->
                            val selectedId = id

                        }
                    }
                    val dialog: AlertDialog? = builder.create()

                    dialog!!.show()


                }
            }
            binding.recyclerView.adapter = adapter
        }else{
            binding.clearbtn.visibility = View.GONE
            binding.refreshbtn.visibility = View.GONE
            binding.refreshbtn2.visibility = View.VISIBLE
        }
    }
    private fun init() {

        with(binding) {
            addbtn.setOnClickListener {
                val intent = Intent(this@MainActivity,MapActivity::class.java)
                startActivity(intent)
            }
            clearbtn.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder!!.setMessage("활동 데이터 채울까요?")
                    .setTitle("채우기")

                builder.apply {
                    setPositiveButton("예") { dialog, id ->
                        val selectedId = id
                        actdata.clear()
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                        val file = File("/data/data/com.example.mindtrip/files/statistics.txt")
                        file.delete()
                        clearbtn.visibility = View.GONE
                        refreshbtn.visibility = View.GONE
                        refreshbtn2.visibility = View.VISIBLE

                    }
                    setNegativeButton("아니요") { dialog, id ->
                        val selectedId = id

                    }
                }
                val dialog: AlertDialog? = builder.create()

                dialog!!.show()


            }
            refreshbtn2.setOnClickListener {
                initRecyclerView()
            }
            refreshbtn.setOnClickListener {
                initRecyclerView()
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
