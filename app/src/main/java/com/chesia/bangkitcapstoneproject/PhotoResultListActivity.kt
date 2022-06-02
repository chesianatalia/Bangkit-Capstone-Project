package com.chesia.bangkitcapstoneproject

<<<<<<< Updated upstream
import android.content.Intent
=======
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
>>>>>>> Stashed changes
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< Updated upstream
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
=======
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
>>>>>>> Stashed changes
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
                    "Don't have permission to access storage.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = PhotoResultListActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

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
<<<<<<< Updated upstream
=======
           
        binding.btNext.setOnClickListener{
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }

            val intent = Intent(this, TFliteActivity::class.java)
            for(i in 0 until list.size){
                listUri.add(getImageUri(this, list[i].photoBitmap, "image$i"))
                Log.d("URI", listUri[i])
            }
            intent.putStringArrayListExtra("listuri", listUri)
            startActivity(intent)
        }
>>>>>>> Stashed changes



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

    companion object{
        const val IMG_BITMAP = "imgbitmap"
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val REQUEST_CODE_PERMISSIONS = 10

    }
}