package com.chesia.bangkitcapstoneproject

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.chesia.bangkitcapstoneproject.databinding.ActivityPhotoResultBinding
import java.io.File

class PhotoResult : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraXButton.setOnClickListener{
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }
        val myFile = intent.getSerializableExtra("picture") as File
        val result = BitmapFactory.decodeFile(myFile.path)
        binding.previewImageView.setImageBitmap(result)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.previewImageView.setImageBitmap(result)
        }
    }

    companion object{
        const val CAMERA_X_RESULT = 200
    }
}