package com.chesia.bangkitcapstoneproject

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chesia.bangkitcapstoneproject.Adapter.PhotoItem
import com.chesia.bangkitcapstoneproject.Adapter.PhotoRVAdapter
import com.chesia.bangkitcapstoneproject.databinding.ActivityPhotoResultListBinding
import java.io.ByteArrayOutputStream
import java.io.File


class PhotoResultListActivity : AppCompatActivity() {
    private lateinit var rvPhotos: RecyclerView;
    private val list = ArrayList<PhotoItem>()
    private val listUri = ArrayList<String>()

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

        binding.cameraXButton3.setOnClickListener{
            val intent = Intent(this, CameraActivity2::class.java)
            launcherIntentCameraX.launch(intent)
        }
           
        binding.btNext.setOnClickListener{
            val intent = Intent(this, TFliteActivity::class.java)
            for(i in 0 until list.size){
                listUri.add(getImageUri(this, list[i].photoBitmap, "image$i"))
                Log.d("URI", listUri[i])
            }
            intent.putStringArrayListExtra("listuri", listUri)
            startActivity(intent)
        }



    }

    private val listPhotos: ArrayList<PhotoItem>
        get(){
            val myFile = if(intent.getSerializableExtra("picture") != null) intent.getSerializableExtra("picture") as File else intent.getSerializableExtra("gallery") as File
            val isBackCamera = intent.getBooleanExtra("isBackCamera", true) as Boolean
            val result = if(intent.getSerializableExtra("picture") != null) rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera) else BitmapFactory.decodeFile(myFile.path)
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
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if(it.data?.getSerializableExtra("picture") != null) it.data?.getSerializableExtra("picture") as File else it.data?.getSerializableExtra("gallery") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = if(it.data?.getSerializableExtra("picture") != null) rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera) else BitmapFactory.decodeFile(myFile.path)
            var photo1 = PhotoItem(result, "Date", "Time")
            list.add(photo1)
            rvPhotos.adapter?.notifyDataSetChanged()

        }
    }
//
//    private val launcherIntentGallery = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == RESULT_OK) {
//            val selectedImg: Uri = result.data?.data as Uri
//            val myFile = uriToFile(selectedImg, this@PhotoResultListActivity)
////            binding.previewImageView.setImageURI(selectedImg)
//        }
//    }

    fun getImageUri(inContext: Context, inImage: Bitmap, title: String): String {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            title,
            null
        )
        return Uri.parse(path).toString()
    }
    companion object{
        const val IMG_BITMAP = "imgbitmap"
        const val CAMERA_X_RESULT = 200

    }
}