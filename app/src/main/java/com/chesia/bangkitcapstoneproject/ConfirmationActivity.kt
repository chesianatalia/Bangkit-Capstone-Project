package com.chesia.bangkitcapstoneproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chesia.bangkitcapstoneproject.databinding.ActivityConfirmationBinding

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonConfirmation.setOnClickListener{
            val intent = Intent(this, TransactionSuccess::class.java)
            startActivity(intent)
        }

    }
}