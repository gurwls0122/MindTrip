package com.example.mindtrip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mindtrip.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    var binding : FragmentHomeBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rdb = Firebase.database.reference
        rdb.get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data ", it)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}