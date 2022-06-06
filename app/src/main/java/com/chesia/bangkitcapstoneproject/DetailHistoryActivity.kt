package com.chesia.bangkitcapstoneproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.chesia.bangkitcapstoneproject.databinding.ActivityDetailHistoryBinding
import com.chesia.bangkitcapstoneproject.databinding.ActivityHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvStatus.text = intent.getStringExtra(EXTRA_STATUS)
            tvNotes.text = intent.getStringExtra(EXTRA_DESCRIPTION)
            tvPoints.text = intent.getStringExtra(EXTRA_POINT)

            Glide.with(this@DetailHistoryActivity)
                .load(intent.getStringExtra(EXTRA_PHOTO))
                .into(imgQrcode)
        }
    }

    companion object{
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_QUANTITY = "extra_quantity"
        const val EXTRA_POINT = "extra_poin"
        const val EXTRA_STATUS = "extra_status"
    }
}