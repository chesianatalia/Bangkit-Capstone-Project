package com.chesia.bangkitcapstoneproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.chesia.bangkitcapstoneproject.Adapter.HistoryAdapter
import com.chesia.bangkitcapstoneproject.Local.LoginPreferences
import com.chesia.bangkitcapstoneproject.databinding.ActivityHistoryBinding


class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var mPreferences: LoginPreferences
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mPreferences = LoginPreferences(this)

        setupView()
        setupViewModel()


        binding.btnBackHistory.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        historyAdapter = HistoryAdapter()
        supportActionBar?.hide()
        binding.apply {
            rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvHistory.adapter = historyAdapter
        }
    }

    private fun setupViewModel(){
        historyViewModel = HistoryViewModel()
        showLoading(true)
        historyViewModel.setHistories(mPreferences.getToken())
        historyViewModel.getHistories().observe(this){
            if(it!= null){
                Log.d("TAG", it[0].point.toString())
                historyAdapter.setListData(it)
                historyAdapter.notifyDataSetChanged()
                showLoading(false)
            } else {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}