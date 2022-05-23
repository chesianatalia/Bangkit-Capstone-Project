package com.chesia.bangkitcapstoneproject

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chesia.bangkitcapstoneproject.Adapter.PhotoItem
import com.chesia.bangkitcapstoneproject.Adapter.PhotoRVAdapter
import com.chesia.bangkitcapstoneproject.databinding.ActivityPhotoResultBinding
import com.chesia.bangkitcapstoneproject.databinding.ActivityPhotoResultListBinding
import java.io.File
import java.lang.reflect.Array.get

class PhotoResultListActivity : AppCompatActivity() {
    private lateinit var rvPhotos: RecyclerView;
    private val list = ArrayList<PhotoItem>()

    private lateinit var binding: ActivityPhotoResultListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvPhotos = findViewById(R.id.rv_photos)
        rvPhotos.setHasFixedSize(true)

        rvPhotos.layoutManager = LinearLayoutManager(this)
        list.addAll(listPhotos)
        showRecyclerList()

        binding.cameraXButton2.setOnClickListener{
            val intent = Intent(this, CameraActivity2::class.java)
            launcherIntentCameraX.launch(intent)
        }



    }

    private val listPhotos: ArrayList<PhotoItem>
        get(){
            val myFile = intent.getSerializableExtra("picture") as File
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), true)
            var photo1 = PhotoItem(result, "Date", "Time")
            val photoList = ArrayList<PhotoItem>()
            photoList.add(photo1)
            return photoList
        }

    private fun showRecyclerList() {
        rvPhotos.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = PhotoRVAdapter(list)
        rvPhotos.adapter = listHeroAdapter
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == PhotoResult.CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
            var photo1 = PhotoItem(result, "Date", "Time")
            list.add(photo1)
            rvPhotos.adapter?.notifyDataSetChanged()

        }
    }

    companion object{
        const val IMG_BITMAP = "imgbitmap"
    }
}