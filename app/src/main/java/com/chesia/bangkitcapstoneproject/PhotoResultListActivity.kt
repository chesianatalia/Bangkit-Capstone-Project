package com.chesia.bangkitcapstoneproject

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chesia.bangkitcapstoneproject.Adapter.PhotoItem
import com.chesia.bangkitcapstoneproject.Adapter.PhotoRVAdapter
import com.chesia.bangkitcapstoneproject.databinding.ActivityPhotoResultListBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PhotoResultListActivity : AppCompatActivity() {
    private lateinit var rvPhotos: RecyclerView
    private val list = ArrayList<PhotoItem>()
    private val listUri = ArrayList<String>()
    private val listQty = ArrayList<Int>()

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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }


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

            val builder = AlertDialog.Builder(this@PhotoResultListActivity)
            builder.setMessage("You cannot go back to this page")
                .setCancelable(false)
                .setPositiveButton("Confirm") { dialog, id ->
                    val intent = Intent(this, TFliteActivity::class.java)
                    for(i in 0 until list.size){
                        //listUri.add(saveToInternalSorage(list[i].photoBitmap))
                        listUri.add(getImageUri(this@PhotoResultListActivity ,list[i].photoBitmap))
                        Log.d("URI", listUri[i])
                    }

                    intent.putStringArrayListExtra("listuri", listUri)
                    intent.putIntegerArrayListExtra("listqty", listQty)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    private val listPhotos: ArrayList<PhotoItem>
        get(){
            showEditQty()
            val myFile = if(intent.getSerializableExtra("picture") != null) intent.getSerializableExtra("picture") as File else intent.getSerializableExtra("gallery") as File
            val isBackCamera = intent.getBooleanExtra("isBackCamera", true) as Boolean
            val result = if(intent.getSerializableExtra("picture") != null) rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera) else BitmapFactory.decodeFile(myFile.path)
            val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            var photo1 = PhotoItem(result, currentDate, "Weight")
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
            showEditQty()
            val myFile = if(it.data?.getSerializableExtra("picture") != null) it.data?.getSerializableExtra("picture") as File else it.data?.getSerializableExtra("gallery") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = if(it.data?.getSerializableExtra("picture") != null) rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera) else BitmapFactory.decodeFile(myFile.path)
            val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            var photo1 = PhotoItem(result, currentDate, "Weight")
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

    fun getImageUri(inContext: Context, inImage: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val filename = "img$ts.jpg"
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            filename,
            null
        )
        return Uri.parse(path).toString()
    }

    private fun saveToInternalSorage(bitmapImage: Bitmap): String {
        val cw = ContextWrapper(applicationContext)
        val directory = cw.getDir("rePlascImg", MODE_PRIVATE)
        // Create imageDir
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val filename = "img$ts.jpg"
        val mypath = File(directory, filename)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("REPLASCIMG", Uri.parse(mypath.toString()).toString())
        return Uri.parse(mypath.toString()).toString()
    }

    private fun showEditQty(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.quantity, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editTextNumber)

        with(builder){
            setTitle("Enter Quantity(Kg)")
            setCancelable(false)
            setPositiveButton("Confirm"){dialog, which->
                if(editText.text.isEmpty()){
                    listQty.add(1)
                    list[list.size-1].Weight = "1 Kg"
                    rvPhotos.adapter?.notifyDataSetChanged()
                }else{
                    listQty.add(editText.text.toString().toInt())
                    list[list.size-1].Weight = editText.text.toString() + " Kg"
                    rvPhotos.adapter?.notifyDataSetChanged()
                }

            }
            setView(dialogLayout)
            show()
        }
    }
    companion object{
        private const val FILENAME_FORMAT = "dd-MMM-yyyy"
        const val IMG_BITMAP = "imgbitmap"
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val REQUEST_CODE_PERMISSIONS = 10

    }
}