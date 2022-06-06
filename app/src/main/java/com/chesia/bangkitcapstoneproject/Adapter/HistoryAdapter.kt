package com.chesia.bangkitcapstoneproject.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chesia.bangkitcapstoneproject.DetailHistoryActivity
import com.chesia.bangkitcapstoneproject.Networking.TrashListItem
import com.chesia.bangkitcapstoneproject.Networking.TrashReportsItem
import com.chesia.bangkitcapstoneproject.R
import com.chesia.bangkitcapstoneproject.TrashList
import com.chesia.bangkitcapstoneproject.databinding.ActivityCardHistoryBinding
import com.chesia.bangkitcapstoneproject.databinding.ItemPhotosBinding
import kotlin.properties.Delegates

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var listHistories : List<TrashReportsItem>? = null
    private var isPET = false
    private var isHDPE = false
    private var isOther = false

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
                    for(i in 0 until history.trashList.size){
                        if(history.trashList[i].category == "PET"){
                            isPET = true
                        }
                        if(history.trashList[i].category == "HDPE"){
                            isHDPE = true
                        }
                        if(history.trashList[i].category == "Other"){
                            isOther = true
                        }
                    }

                    binding.apply {
                        tvStatus.text = history.status
                        if(isPET){
                            tvCategory1.text = "PET"
                        }
                        if(isHDPE){
                            tvCategory2.text = "HDPE"
                        }
                        if(isOther){
                            tvCategory3.text = "Other"
                        }
                        tvQuantity1.text = history.trashList[0].quantity.toString()

                        Glide.with(itemView)
                            .load(history.trashList[0].photo)
                            .into(imgBarcode)

                    }

                    itemView.setOnClickListener{
                        val intent = Intent(itemView.context, DetailHistoryActivity::class.java).also {
                            it.putExtra(DetailHistoryActivity.EXTRA_STATUS, history.status)
                            it.putExtra(DetailHistoryActivity.EXTRA_DESCRIPTION, history.description)
                            it.putExtra(DetailHistoryActivity.EXTRA_POINT, history.point.toString())
                            it.putExtra(DetailHistoryActivity.EXTRA_PHOTO, history.trashList[0].photo)
                        }
                        itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity)
                            .toBundle())

                    }

                }

    }

    companion object{

    }

}