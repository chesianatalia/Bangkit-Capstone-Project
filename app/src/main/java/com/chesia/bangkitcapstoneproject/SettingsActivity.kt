package com.chesia.bangkitcapstoneproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.chesia.bangkitcapstoneproject.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

    }

    private fun setupAction(){
        binding.tvEditProfile.setOnClickListener {

        }

        binding.tvEditPin.setOnClickListener {

        }

        binding.tvBahasa.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        binding.tvTema.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.tvKodeReferal.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.tvAbout.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.tvCredit.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.tvDelete.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }

}