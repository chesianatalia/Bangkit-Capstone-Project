package com.chesia.bangkitcapstoneproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.chesia.bangkitcapstoneproject.Local.LoginPreferences
import com.chesia.bangkitcapstoneproject.Networking.ApiConfig
import com.chesia.bangkitcapstoneproject.Networking.GetTrashResponse
import com.chesia.bangkitcapstoneproject.Networking.Maplist.MapListResponse
import com.chesia.bangkitcapstoneproject.Networking.Newslist.NewsListResponse
import com.chesia.bangkitcapstoneproject.Networking.UserProfileResponse
import com.chesia.bangkitcapstoneproject.databinding.ActivityHomepageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HomepageActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var binding : ActivityHomepageBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var mPreferences: LoginPreferences
    private lateinit var mMap: GoogleMap
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val listlatlon = ArrayList<LatLng>()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Don't have permission to access camera.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        setSupportActionBar(binding.toolbar)

        mPreferences = LoginPreferences(this)

        val token = mPreferences.getToken()
        getUserData(token)
//         getPointUser(token)
        getNews(token)

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnHistories.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnScan.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)

            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
        setupView()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toolBar: Toolbar = binding.toolbar


        toggle = ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.black)


        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.itemIconTintList = null
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.close_nav -> drawerLayout.closeDrawer(GravityCompat.START)
                R.id.pengaturan -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.log_out -> logOut()
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        mPreferences.clearPreference()
        val intent = Intent(this, MainActivity::class.java)

        var user = auth.currentUser
        if(user?.email.toString() == "null"){
            startActivity(intent)
            finish()
        }else{
            auth.signOut()
            googleSignInClient.signOut().addOnCompleteListener(this){
                startActivity(intent)
                finish()
            }
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
    }

    private fun getNews(token: String) {
        ApiConfig.getApiService().getNewsList(token = "Bearer $token")
            .enqueue(object : Callback<NewsListResponse> {
                override fun onResponse(
                    call: Call<NewsListResponse>,
                    response: Response<NewsListResponse>
                ) {
                    if(response.isSuccessful && response.body() != null){
                        Glide.with(this@HomepageActivity).load(response.body()!!.data!!.news!![0]!!.image).fitCenter().into(binding.imgNews)
                        binding.tvNews.text = response.body()!!.data!!.news!![0]!!.description
                    }
                }

                override fun onFailure(call: Call<NewsListResponse>, t: Throwable) {
                    Toast.makeText(this@HomepageActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getUserData(token:String){
        showLoading(true)

        ApiConfig.getApiService().getUserProfile(token = "Bearer $token")
            .enqueue(object : Callback<UserProfileResponse>{
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("Response", response.body()?.success.toString())
                    binding.apply {
//                        Glide.with(this@HomepageActivity).load(response.body()?.data?.user?.photoUrl).circleCrop().into(binding.imgUser)
                        tvUserName.text = response.body()?.data?.user?.fullname
                        tvUserPhoneNumber.text = response.body()?.data?.user?.meta?.phone
                        tvUserEmail.text = response.body()?.data?.user?.email
//                        tvUserPoint.text = "${response.body()?.data?.user?.status} points"
                    }
                    showLoading(false)
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@HomepageActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }


//    private fun getPointUser(token:String){
//        ApiConfig.getApiService().getHistory(token = "Bearer $token").enqueue(object : Callback<GetTrashResponse>{
//            override fun onResponse(
//                call: Call<GetTrashResponse>,
//                response: Response<GetTrashResponse>
//            ) {
//                if(response.isSuccessful){
//                    if(response.body()?.data?.trashReports?.get(0)?.point ?: =null){
//
//                    }
//                    binding.tvUserPoint.text = "${response.body()?.data?.trashReports?.get(0)?.point} points"
//                }
//            }
//
//            override fun onFailure(call: Call<GetTrashResponse>, t: Throwable) {
//                Toast.makeText(this@HomepageActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }


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
                    Toast.makeText(this@HomepageActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}