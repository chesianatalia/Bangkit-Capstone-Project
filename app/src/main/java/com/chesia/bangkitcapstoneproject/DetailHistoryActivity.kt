package com.chesia.bangkitcapstoneproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chesia.bangkitcapstoneproject.databinding.ActivityHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
<<<<<<< Updated upstream
=======

        binding.apply {
            tvStatus.text = intent.getStringExtra(EXTRA_STATUS)
            tvNotes.text = intent.getStringExtra(EXTRA_DESCRIPTION)
            tvPoints.text = intent.getStringExtra(EXTRA_POINT)

            Glide.with(this@DetailHistoryActivity)
                .load(intent.getStringExtra(EXTRA_PHOTO))
                .into(imgQrcode)
        }

        binding.btnBackDetail.setOnClickListener {
            val intent = Intent(this@DetailHistoryActivity,HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    companion object{
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_QUANTITY = "extra_quantity"
        const val EXTRA_POINT = "extra_poin"
        const val EXTRA_STATUS = "extra_status"
>>>>>>> Stashed changes
    }
}