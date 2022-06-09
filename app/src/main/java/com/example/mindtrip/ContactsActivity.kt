package com.example.mindtrip

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindtrip.databinding.ActivityContactsBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContactsActivity : AppCompatActivity() {

    lateinit var binding: ActivityContactsBinding
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: PhoneAdapter
    lateinit var rdb: DatabaseReference
    val CALL_REQUEST = 100
    var findQuery = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun callAction(){
        val number = Uri.parse("tel:" + binding.TelEditText.text.toString())
        val CallIntent = Intent(Intent.ACTION_CALL, number)
        when{
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED->{
                startActivity(CallIntent)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)
            ->{
                //거부했을 때
            }
            else->{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),CALL_REQUEST)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CALL_REQUEST->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"권한 승인 완료", Toast.LENGTH_SHORT).show()
                    callAction()
                }else{
                    Toast.makeText(this,"권한승인이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun init(){
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rdb = Firebase.database.getReference("Contacts/items")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<ContactData>()
            .setQuery(query,ContactData::class.java)
            .build()
        adapter = PhoneAdapter(option)
        adapter.itemClickListener = object:PhoneAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                binding.apply {
                    NameEditText.setText(adapter.getItem(position).name)
                    TelEditText.setText(adapter.getItem(position).tel)
                }
            }
        }

        binding.apply {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

            addbtn.setOnClickListener {
                initQuery()
                val item = ContactData(NameEditText.text.toString(),
                    TelEditText.text.toString())
                rdb.child(NameEditText.text.toString()).setValue(item)
                clearInput()
            }

            searchbtn.setOnClickListener {
                if(!findQuery)
                    findQuery = true
                if (adapter!=null)
                    adapter.stopListening()
                val query = rdb.orderByChild("name").equalTo(NameEditText.text.toString())
                val option = FirebaseRecyclerOptions.Builder<ContactData>()
                    .setQuery(query,ContactData::class.java)
                    .build()
                adapter = PhoneAdapter(option)
                adapter.itemClickListener = object:PhoneAdapter.OnItemClickListener {
                    override fun OnItemClick(position: Int) {
                        binding.apply {
                            NameEditText.setText(adapter.getItem(position).name)
                            TelEditText.setText(adapter.getItem(position).tel)
                        }
                    }
                }
                recyclerView.adapter = adapter
                adapter.startListening()
                clearInput()
            }

            deletebtn.setOnClickListener {
                initQuery()
                rdb.child(NameEditText.text.toString()).removeValue()
                clearInput()
            }

            callbtn.setOnClickListener {
                callAction()
            }

        }

    }

    fun initQuery(){
        if(findQuery)
            findQuery = false
        if (adapter!=null)
            adapter.stopListening()
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<ContactData>()
            .setQuery(query,ContactData::class.java)
            .build()
        adapter = PhoneAdapter(option)
        adapter.itemClickListener = object:PhoneAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                binding.apply {
                    NameEditText.setText(adapter.getItem(position).name)
                    TelEditText.setText(adapter.getItem(position).tel)
                }
            }
        }

        binding.recyclerView.adapter = adapter
        adapter.startListening()
    }

    fun clearInput(){
        binding.apply {
            NameEditText.text.clear()
            TelEditText.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

}