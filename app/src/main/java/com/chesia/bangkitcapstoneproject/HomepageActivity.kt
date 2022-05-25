package com.chesia.bangkitcapstoneproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.chesia.bangkitcapstoneproject.Local.UserPreferences
import com.chesia.bangkitcapstoneproject.databinding.ActivityHomepageBinding
import com.google.android.material.navigation.NavigationView

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomepageBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var mPreferences: UserPreferences

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSIONS){
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
        setSupportActionBar(binding.tbHomepage)

        mPreferences = UserPreferences(this)

        binding.btnScan.setOnClickListener{
            val Intent = Intent(this, CameraActivity::class.java)
            startActivity(Intent)

            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }

        binding.btnHistories.setOnClickListener{
            mPreferences.clearPreference()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toolbar: androidx.appcompat.widget.Toolbar = binding.tbHomepage



//        toggle = actionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)

//        toggle = actionBarDrawerToggle(this, drawerLayout, binding.menu, R.string.open, R.string.close)




//        toggle.isDrawerIndicatorEnabled = true
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()


//        toggle.isDrawerIndicatorEnabled = true
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()


    }



    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}