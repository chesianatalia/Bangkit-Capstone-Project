package com.chesia.bangkitcapstoneproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chesia.bangkitcapstoneproject.Networking.TrashListItem
import com.chesia.bangkitcapstoneproject.Networking.TrashReportsItem
import com.chesia.bangkitcapstoneproject.R
import com.chesia.bangkitcapstoneproject.TrashList
import com.chesia.bangkitcapstoneproject.databinding.ActivityCardHistoryBinding
import com.chesia.bangkitcapstoneproject.databinding.ItemPhotosBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private var listHistory: List<TrashReportsItem>? = null


    fun setListData(histories: List<TrashReportsItem>?) {
        this.listHistory = histories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            ActivityCardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(listHistory?.get(position)!!)

    }

    override fun getItemCount(): Int {
        return if (listHistory == null) 0
        else listHistory?.size!!
    }


    inner class HistoryViewHolder(private val binding: ActivityCardHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: TrashReportsItem) {
            binding.apply {
                tvStatus.text = history.status
                tvDate.text = history.createdAt.toString()
                tvCategory1.text = history.trashList[0].category
                tvCategory2.text = history.trashList[1].category
                tvCategory3.text = history.trashList[2].category
                tvQuantity1.text = history.trashList[0].quantity.toString()
                tvQuantity2.text = history.trashList[1].quantity.toString()
                tvQuantity3.text = history.trashList[2].quantity.toString()
            }
        }
    }
}