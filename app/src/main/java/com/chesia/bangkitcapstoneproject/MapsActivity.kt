package com.chesia.bangkitcapstoneproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.chesia.bangkitcapstoneproject.Local.LoginPreferences
import com.chesia.bangkitcapstoneproject.Networking.ApiConfig
import com.chesia.bangkitcapstoneproject.Networking.Maplist.MapListResponse

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.chesia.bangkitcapstoneproject.databinding.ActivityMapsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mPreferences: LoginPreferences
    private val listlatlon = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPreferences = LoginPreferences(this)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listlatlon[0], 15f))
                            mMap.addMarker(MarkerOptions().position(LatLng(lat, lon)).snippet(snippet))
                        }
                    }
                }

                override fun onFailure(call: Call<MapListResponse>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}