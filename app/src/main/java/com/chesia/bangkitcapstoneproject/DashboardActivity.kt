package com.chesia.bangkitcapstoneproject
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.chesia.bangkitcapstoneproject.databinding.ActivityDashboardBinding
//
//class DashboardActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityDashboardBinding
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode == REQUEST_CODE_PERMISSIONS){
//            if (!allPermissionsGranted()) {
//                Toast.makeText(
//                    this,
//                    "Don't have permission to access camera.",
//                    Toast.LENGTH_SHORT
//                ).show()
//                finish()
//            }
//        }
//    }
//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDashboardBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.scanButton.setOnClickListener{
//            val intent = Intent(this,CameraActivity::class.java)
//            startActivity(intent)
//
//            if (!allPermissionsGranted()) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    REQUIRED_PERMISSIONS,
//                    REQUEST_CODE_PERMISSIONS
//                )
//            }
//        }
//        binding.settingButton.setOnClickListener{
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    companion object {
//        const val CAMERA_X_RESULT = 200
//        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//        private const val REQUEST_CODE_PERMISSIONS = 10
//    }
//}
