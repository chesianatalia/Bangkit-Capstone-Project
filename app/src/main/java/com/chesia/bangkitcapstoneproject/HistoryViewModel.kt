package com.chesia.bangkitcapstoneproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chesia.bangkitcapstoneproject.Networking.ApiConfig
import com.chesia.bangkitcapstoneproject.Networking.GetTrashResponse
import com.chesia.bangkitcapstoneproject.Networking.TrashReportsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel(){
    private val listHistories = MutableLiveData<List<TrashReportsItem>?>()

    fun setHistories(Token: String){
        val client = ApiConfig.getApiService().getHistory(token = "Bearer $Token")
        client.enqueue(object : Callback<GetTrashResponse> {
            override fun onResponse(
                call: Call<GetTrashResponse>,
                response: Response<GetTrashResponse>
            ) {
                if(response.isSuccessful){
                    listHistories.postValue(response.body()?.data?.trashReports)
                } else {
                    listHistories.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetTrashResponse>, t: Throwable) {
                listHistories.postValue(null)
            }
        })
    }

    fun getHistories(): MutableLiveData<List<TrashReportsItem>?>{
        return listHistories
    }
}