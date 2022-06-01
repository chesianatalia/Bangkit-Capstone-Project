package com.chesia.bangkitcapstoneproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.tensorflow.lite.Interpreter
import com.chesia.bangkitcapstoneproject.databinding.ActivityMainBinding
import com.chesia.bangkitcapstoneproject.databinding.ActivityTfliteBinding

class TFliteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTfliteBinding
    private lateinit var interpreter : Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTfliteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        interpreter =

    }
}