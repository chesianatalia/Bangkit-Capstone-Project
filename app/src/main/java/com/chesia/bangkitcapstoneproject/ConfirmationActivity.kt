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
        binding.textViewPete.text = intent.getIntExtra("totalPET", 0).toString()
        binding.textViewhdpe.text = intent.getIntExtra("totalHDPE", 0).toString()
        binding.textViewOther.text = intent.getIntExtra("totalOTHER", 0).toString()
        binding.tvNotes.text = intent.getStringExtra("note")
        
        binding.buttonConfirmation.setOnClickListener{
            val intent = Intent(this, TransactionSuccess::class.java)
            startActivity(intent)
        }

    }
}