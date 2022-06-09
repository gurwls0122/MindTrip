package com.example.mindtrip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindtrip.DiaryAdapter
import com.example.mindtrip.DiaryData
import com.example.mindtrip.DiaryWriteActivity
import com.example.mindtrip.databinding.FragmentDiaryBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DiaryFragment : Fragment() {
    lateinit var binding: FragmentDiaryBinding
    lateinit var mAdapter: DiaryAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var rdb: DatabaseReference
    val monthItems = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12")
    val yearItems = arrayOf("2018","2019","2020","2021","2022")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiaryBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //spinner
        initSpinner()
        initRecyclerview()

        //write new diary
        binding.writebtn.setOnClickListener {
            val intent = Intent(this.context, DiaryWriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerview() {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        rdb = Firebase.database.getReference("Diary/Contents")
        rdb.get().addOnSuccessListener {
            Log.i("TAG", it.value.toString())
        }
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<DiaryData>()
            .setQuery(query, DiaryData::class.java).build()

        //edit diary
        mAdapter = DiaryAdapter(option)
        mAdapter.itemClickListener = object : DiaryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), DiaryWriteActivity::class.java)
                intent.putExtra("position", position)
                startActivity(intent)
            }
        }
        binding.apply {
            diaryRecyclerview.layoutManager = layoutManager
            diaryRecyclerview.adapter = mAdapter
        }
    }

    private fun initSpinner() {
        val adapter2 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,yearItems)
        val adapter3 = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,monthItems)

        binding.apply {
            yearSpinner.adapter = adapter2
            yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                    val yearkey = parent?.getItemAtPosition(position).toString()
                    println(yearkey)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
            monthSpinner.adapter = adapter3
            monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                    val monthkey=parent?.getItemAtPosition(position).toString()
                    println(monthkey)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }


    }



}