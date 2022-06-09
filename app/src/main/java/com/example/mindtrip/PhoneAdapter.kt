package com.example.mindtrip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindtrip.databinding.RowContactsBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class PhoneAdapter(options: FirebaseRecyclerOptions<ContactData>):FirebaseRecyclerAdapter<ContactData, PhoneAdapter.ViewHolder>(options) {
    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RowContactsBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClickListener!!.OnItemClick(bindingAdapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowContactsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ContactData) {
        holder.binding.apply {
            name.text = model.name
            telephone.text = model.tel
        }
    }
}