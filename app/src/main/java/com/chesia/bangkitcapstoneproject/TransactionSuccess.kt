package com.chesia.bangkitcapstoneproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chesia.bangkitcapstoneproject.databinding.ActivityTransactionSuccessBinding

class TransactionSuccess : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backHome.setOnClickListener{
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
        }
    }
}