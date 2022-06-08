package com.example.mindtrip

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindtrip.databinding.ActRowBinding

class ActAdapter(val items:ArrayList<ActData>): RecyclerView.Adapter<ActAdapter.ViewHolder>(){

    interface OnItemClickListener{
        fun OnItemClick(data:ActData)
    }
    var itemClickListener: OnItemClickListener?=null
    inner class ViewHolder(val binding: ActRowBinding)  :  RecyclerView.ViewHolder(binding.root) {
        init{
            binding.textView.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = items[position].name
        holder.binding.calView.text = items[position].type
    }
}