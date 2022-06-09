package com.example.dmindtrip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dmindtrip.databinding.RowDiaryBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class DiaryAdapter(options: FirebaseRecyclerOptions<DiaryData>):
FirebaseRecyclerAdapter<DiaryData, DiaryAdapter.ViewHolder>(options)  {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var itemClickListener:OnItemClickListener?= null
    inner class ViewHolder(val binding:RowDiaryBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClickListener!!.onItemClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model:DiaryData) {
        holder.binding.apply {
            diaryDate.text = model.month.toString() + "/"+ model.day.toString()
            diaryTitle.text = model.title
        }

    }


}
