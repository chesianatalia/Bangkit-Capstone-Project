package com.chesia.bangkitcapstoneproject.Adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
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
import java.util.ArrayList
import kotlin.properties.Delegates
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var listHistories: List<TrashReportsItem>? = null

    fun setListData(histories: List<TrashReportsItem>?){
        this.listHistories = histories
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            ActivityCardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        val listCat = ArrayList<String>()
        val listQty = ArrayList<Int>()
        for (i in 0 until listHistories?.get(position)!!.trashList.size) {
            listCat.add(listHistories?.get(position)!!.trashList[i].category!!)
            listQty.add(listHistories?.get(position)!!.trashList[i].quantity)
        }

        val listQtyUnique = mutableListOf(0, 0, 0)

        for (i in listQty.indices) {
            if(listCat[i] == "PET"){
                listQtyUnique[0] = listQtyUnique[0] + listQty[i]
            }
            if(listCat[i] == "HDPE"){
                listQtyUnique[1] = listQtyUnique[1] + listQty[i]
            }
            if(listCat[i].lowercase() == "other"){
                listQtyUnique[2] = listQtyUnique[2] + listQty[i]
            }
        }

        holder.bind(listHistories?.get(position)!!, listQtyUnique)
    }

    override fun getItemCount(): Int {
        return if (listHistories == null) 0
        else listHistories!!.size
    }

    inner class HistoryViewHolder(private val binding: ActivityCardHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            history: TrashReportsItem,
            listQty: MutableList<Int>
        ) {

            val barcodeEncoder = BarcodeEncoder()
            val qrBitmap = barcodeEncoder.encodeBitmap(history.id, BarcodeFormat.QR_CODE, 512, 512)

            binding.apply {
                tvStatus.text = history.status
                tvQuantity1.text = history.trashList[0].quantity.toString()
                tvStatus.text = history.status

                tvCategory1.text = "PET"
                tvCategory2.text = "HDPE"
                tvCategory3.text = "Other"

                tvQuantity1.text = listQty[0].toString()
                tvQuantity2.text = listQty[1].toString()
                tvQuantity3.text = listQty[2].toString()

                Glide.with(itemView)
                    .load(qrBitmap)
                    .into(imgBarcode)

            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailHistoryActivity::class.java).also {
                    it.putExtra(DetailHistoryActivity.EXTRA_STATUS, history.status)
                    it.putExtra(DetailHistoryActivity.EXTRA_DESCRIPTION, history.description)
                    it.putExtra(DetailHistoryActivity.EXTRA_POINT, history.point.toString())
                    it.putExtra(DetailHistoryActivity.EXTRA_ID, history.id)
                }
                itemView.context.startActivity(
                    intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity)
                        .toBundle()
                )
            }
        }
    }
}