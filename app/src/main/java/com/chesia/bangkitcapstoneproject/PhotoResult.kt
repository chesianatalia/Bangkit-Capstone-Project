package com.chesia.bangkitcapstoneproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import com.chesia.bangkitcapstoneproject.Adapter.PhotoItem
import com.chesia.bangkitcapstoneproject.databinding.ActivityPhotoResultBinding
import java.io.ByteArrayOutputStream
import java.io.File

class PhotoResult : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoResultBinding
    private lateinit var photoList: ArrayList<PhotoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraXButton.setOnClickListener{
            val intent = Intent(this, CameraActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("origin", 1)
            launcherIntentCameraX.launch(intent)
        }

        val myFile = intent.getSerializableExtra("picture") as File
        val isBackCamera = intent.getBooleanExtra("isBackCamera", true) as Boolean
        val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
        binding.previewImageView.setImageBitmap(result)

        binding.nextPage.setOnClickListener{
            val intent = Intent(this, PhotoResultListActivity::class.java)
            startActivity(intent)

        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    companion object{
        const val CAMERA_X_RESULT = 200
    }
}