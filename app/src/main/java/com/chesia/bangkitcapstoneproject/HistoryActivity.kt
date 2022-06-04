package com.chesia.bangkitcapstoneproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.chesia.bangkitcapstoneproject.Adapter.HistoryAdapter
import com.chesia.bangkitcapstoneproject.Local.LoginPreferences
import com.chesia.bangkitcapstoneproject.Networking.ApiConfig
import com.chesia.bangkitcapstoneproject.Networking.GetTrashResponse
import com.chesia.bangkitcapstoneproject.Networking.TrashReportsItem
import com.chesia.bangkitcapstoneproject.databinding.ActivityHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var mPreferences: LoginPreferences
    private val listHistories = MutableLiveData<List<TrashReportsItem>?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackHistory.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        historyAdapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
        binding.rvHistory.adapter = historyAdapter

        getHistory(mPreferences.getToken())
//        historyAdapter.setListData()

    }

    private fun getHistory(Token: String){
        val client = ApiConfig.getApiService().getHistory(token = "Bearer $Token")
        client.enqueue(object : Callback<GetTrashResponse>{
            override fun onResponse(
                call: Call<GetTrashResponse>,
                response: Response<GetTrashResponse>
            ) {
                if(response.isSuccessful){
                    listHistories.postValue(response.body()?.data)
                } else {
                    listHistories.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetTrashResponse>, t: Throwable) {
                listHistories.postValue(null)
            }
        })

    }
}