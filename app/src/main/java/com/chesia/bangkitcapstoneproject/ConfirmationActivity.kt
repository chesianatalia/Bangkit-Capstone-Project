package com.chesia.bangkitcapstoneproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chesia.bangkitcapstoneproject.Local.LoginPreferences
import com.chesia.bangkitcapstoneproject.Networking.ApiConfig
import com.chesia.bangkitcapstoneproject.Networking.Maplist.MapListResponse
import com.chesia.bangkitcapstoneproject.Networking.TrashReportResponse
import com.chesia.bangkitcapstoneproject.databinding.ActivityConfirmationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import java.io.File
import java.util.*


class ConfirmationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding:ActivityConfirmationBinding
    private lateinit var mPreferences: LoginPreferences
    private val trashImagesParts = ArrayList<MultipartBody.Part?>()
    private lateinit var mMap: GoogleMap
    private val listlatlon = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val totalPET = intent.getIntExtra("totalPET", 0)
        val totalHDPE = intent.getIntExtra("totalHDPE", 0)
        val totalOTHER = intent.getIntExtra("totalOTHER", 0)
        val totalPoint = (totalPET * 5) + (totalHDPE * 5) + (totalOTHER * 10)

        binding.textViewPete.text = totalPET.toString()
        binding.textViewhdpe.text = totalHDPE.toString()
        binding.textViewOther.text = totalOTHER.toString()
        binding.tvPoints.text = totalPoint.toString()
        binding.tvNotes.text = intent.getStringExtra("note")

        val photoListUri = intent.getStringArrayListExtra("photolist")

        mPreferences = LoginPreferences(this)
        binding.buttonConfirmation.setOnClickListener{
            loading(true)
            val titlePart = "Title".toRequestBody(MultipartBody.FORM)
            val descriptionPart = binding.tvNotes.text.toString().toRequestBody(MultipartBody.FORM)
            val trashListPart = intent.getStringExtra("trashlist")!!.toRequestBody(MultipartBody.FORM)
            val pointPart = totalPoint.toString().toRequestBody(MultipartBody.FORM)
            val collectionPointPart = "20".toRequestBody(MultipartBody.FORM)

            for(i in 0 until photoListUri!!.size){
                val imgFile = uriToFile(Uri.parse(photoListUri[i]), this@ConfirmationActivity)
                val requestImageFile = imgFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photos",
                    imgFile.name,
                    requestImageFile
                )

                trashImagesParts.add(imageMultipart)
            }

            val client = ApiConfig.getApiService().sendTrashReport("Bearer " + mPreferences.getToken(), titlePart, descriptionPart, trashImagesParts, trashListPart, pointPart, collectionPointPart)
            client.enqueue(object: Callback<TrashReportResponse>{
                override fun onResponse(
                    call: Call<TrashReportResponse>,
                    response: Response<TrashReportResponse>
                ) {
                    if(response.isSuccessful && response.body() != null){
                        Toast.makeText(this@ConfirmationActivity, "Trash report sent!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ConfirmationActivity, TransactionSuccess::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<TrashReportResponse>, t: Throwable) {
                    loading(false)
                    Toast.makeText(this@ConfirmationActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                    Log.d("Trasherrorreport", t.message.toString())
                }
            })
        }
    }

    private fun loading(isLoading: Boolean){
        if(isLoading){
            binding.pbTrashreport.visibility = View.VISIBLE
            binding.buttonConfirmation.isEnabled = false
        }else{
            binding.pbTrashreport.visibility = View.GONE
            binding.buttonConfirmation.isEnabled = true
        }
    }

    fun intentMaps(view: View) {
        startActivity(Intent(this@ConfirmationActivity, MapsActivity::class.java))
    }
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        ApiConfig.getApiService().getMapList(token = "Bearer ${mPreferences.getToken()}")
            .enqueue(object : Callback<MapListResponse> {
                override fun onResponse(
                    call: Call<MapListResponse>,
                    response: Response<MapListResponse>
                ) {
                    if(response.isSuccessful && response.body() != null){
                        for(i in 0 until response.body()!!.data!!.collectionPoints!!.size){
                            val lat = response.body()!!.data!!.collectionPoints!![i]?.latitude!!
                            val lon = response.body()!!.data!!.collectionPoints!![i]?.longitude!!
                            val snippet = response.body()!!.data!!.collectionPoints!![i]?.description
                            listlatlon.add(LatLng(lat, lon))
                            binding.location.text = snippet
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listlatlon[0], 15f))
                            mMap.addMarker(MarkerOptions().position(LatLng(lat, lon)).snippet(snippet))
                        }
                    }
                }

                override fun onFailure(call: Call<MapListResponse>, t: Throwable) {
                    Toast.makeText(this@ConfirmationActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}