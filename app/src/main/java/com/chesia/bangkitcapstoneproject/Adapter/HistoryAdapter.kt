package com.chesia.bangkitcapstoneproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesia.bangkitcapstoneproject.Networking.TrashListItem
import com.chesia.bangkitcapstoneproject.Networking.TrashReportsItem
import com.chesia.bangkitcapstoneproject.R
import com.chesia.bangkitcapstoneproject.TrashList
import com.chesia.bangkitcapstoneproject.databinding.ActivityCardHistoryBinding
import com.chesia.bangkitcapstoneproject.databinding.ItemPhotosBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var listHistories : List<TrashReportsItem>? = null

    fun setListData(histories: List<TrashReportsItem>?){
        this.listHistories = histories
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = ActivityCardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(listHistories?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return if(listHistories == null) 0
        else listHistories!!.size
    }

    inner class HistoryViewHolder(private val binding: ActivityCardHistoryBinding) :
            RecyclerView.ViewHolder(binding.root){
                fun bind(history: TrashReportsItem){
                    binding.apply {
                        tvStatus.text = history.status
                        tvCategory1.text = history.trashList[0]?.category
                        tvQuantity1.text = history.trashList[0].quantity?.toString()

                        Glide.with(itemView)
                            .load(history.trashList[0].photo)
                            .into(imgBarcode)

                    }

                }

    }

}