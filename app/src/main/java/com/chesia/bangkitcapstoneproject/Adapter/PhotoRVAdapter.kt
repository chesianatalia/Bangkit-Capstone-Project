package com.chesia.bangkitcapstoneproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chesia.bangkitcapstoneproject.R

class PhotoRVAdapter(private val photoList: ArrayList<PhotoItem>) : RecyclerView.Adapter<PhotoRVAdapter.Holder>(){
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val imgDate: TextView = itemView.findViewById(R.id.tv_photoDate)
        val imgTime: TextView = itemView.findViewById(R.id.tv_photoTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photos, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.imgPhoto.setImageBitmap(photoList[position].photoBitmap)
        holder.imgDate.text = photoList[position].photoDate
        holder.imgTime.text = photoList[position].Weight
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}