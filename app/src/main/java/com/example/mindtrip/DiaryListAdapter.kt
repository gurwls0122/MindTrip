package com.example.mindtrip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dmindtrip.databinding.RowDiaryBinding

class DiaryListAdapter(val diarylists: ArrayList<DiaryData>): RecyclerView.Adapter<DiaryListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(data: DiaryData, position: Int)
    }

    var itemClickListener:OnItemClickListener?= null

    inner class ViewHolder(val binding: RowDiaryBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.diaryItem.setOnClickListener {
                itemClickListener?.onItemClick(diarylists[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowDiaryBinding.inflate(LayoutInflater.from(parent.context),parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diarylists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.diaryDate.text = diarylists[position].month.toString() + "."+ diarylists[position].day.toString()
        holder.binding.diaryTitle.text = diarylists[position].title

    }
}
