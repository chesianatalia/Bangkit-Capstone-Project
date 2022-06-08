package com.chesia.bangkitcapstoneproject


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.content.ContentResolver
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.chesia.bangkitcapstoneproject.databinding.ActivityTfliteBinding
import com.chesia.bangkitcapstoneproject.ml.Model2
import com.chesia.bangkitcapstoneproject.ml.Model3
import com.google.gson.Gson
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TFliteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTfliteBinding
    private val trashList = ArrayList<TrashList>()
    private val categoryList = ArrayList<String>()
    private var imgSize: Int = 224
    var PET = 0
    var HDPE = 0
    var Other = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTfliteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoList = intent.getStringArrayListExtra("listuri")
        Log.d("TFLITEURI", photoList!![0])
        val quantityList: ArrayList<Int>? = intent.getIntegerArrayListExtra("listqty")


        val size = photoList!!.size
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        for (i in 0 until photoList!!.size) {
            if(doInference(convertBmp(getBitmap(contentResolver, Uri.parse(photoList[i]))!!)!!) == "PET"){
                categoryList.add("PET")
                PET += quantityList!![i]
            }
            if(doInference(convertBmp(getBitmap(contentResolver, Uri.parse(photoList[i]))!!)!!) == "HDPE"){
                categoryList.add("HDPE")
                HDPE += quantityList!![i]
            }
            if(doInference(convertBmp(getBitmap(contentResolver, Uri.parse(photoList[i]))!!)!!) == "Other"){
                categoryList.add("Other")
                Other += quantityList!![i]
            }
        }

        Log.d("photolistsize", photoList.size.toString())

        binding.tvPET.text = PET.toString()
        binding.tvHDPE.text = HDPE.toString()
        binding.tvOTHER.text = Other.toString()

      binding.confirmButton.setOnClickListener{
          for(i in 0 until photoList!!.size){
              trashList.add(TrashList("Sampah " + (i+1), categoryList[i], quantityList!![i], currentDate))
          }
          val gson = Gson()
          val trashListJson = gson.toJson(trashList)

          Log.d("JSON", trashListJson)

        val intent = Intent(this, ConfirmationActivity::class.java)
          intent.putExtra("totalPET", PET)
          intent.putExtra("totalHDPE", HDPE)
          intent.putExtra("totalOTHER", Other)
          intent.putExtra("note", binding.notesInput.editText!!.text.toString())
          intent.putExtra("trashlist", trashListJson)
          intent.putExtra("photolist", photoList)
          intent.putExtra("categorylist", categoryList)
        startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@TFliteActivity)
        builder.setMessage("Cancel process?")
            .setCancelable(false)
            .setPositiveButton("Confirm") { dialog, id ->
                finish()
            }
            .setNegativeButton("No", null)
        val alert = builder.create()
        alert.show()
    }

    private fun doInference(bmp: Bitmap) : String{
        val scaledBmp = Bitmap.createScaledBitmap(bmp, imgSize, imgSize, false)
        return classifyImage(scaledBmp)
    }

    private fun classifyImage(image: Bitmap) : String {
        try {
            val model = Model3.newInstance(applicationContext)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imgSize * imgSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imgSize * imgSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0

            for (i in 0 until imgSize) {
                for (j in 0 until imgSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray

            var maxPos = 0
            var maxConfidence = -10f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val petc = confidences[0]
            val hdpec = confidences[1]
            val otherc = confidences[2]
            Log.d("confidences", "PET: $petc")
            Log.d("confidences", "HDPE: $hdpec")
            Log.d("confidences", "other: $otherc")
            Log.d("confidences", "maxcon: $maxConfidence")
            Log.d("confidences", "maxpos: $maxPos")
            val classes = arrayOf("PET", "HDPE", "Other")
            val class_: String = classes[maxPos]
            Log.d("confidences", "CLASS: $class_")
            //binding.tvModelresult.text = classes[maxPos]

            model.close()
            return classes[maxPos]
        } catch (e: IOException) {
            return ""
        }
    }

    fun getBitmap(contentResolver: ContentResolver, fileUri: Uri?): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri!!))
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
            }
        } catch (e: Exception){
            null
        }
    }

    private fun convertBmp(bitmap: Bitmap): Bitmap? {
        return bitmap.copy(Bitmap.Config.RGB_565, false)
    }
}