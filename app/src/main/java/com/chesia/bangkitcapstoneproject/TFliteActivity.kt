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
import com.chesia.bangkitcapstoneproject.databinding.ActivityTfliteBinding
import com.chesia.bangkitcapstoneproject.ml.Model2
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


class TFliteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTfliteBinding
    private var imgSize: Int = 224


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTfliteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoList = intent.getStringArrayListExtra("listuri")

        var PET = 0
        var HDPE = 0
        var Other = 0
        val size = photoList!!.size

        for (i in 0 until photoList!!.size) {
            if(doInference(convertBmp(getBitmap(contentResolver, Uri.parse(photoList[i]))!!)!!) == "PET"){
                PET += 1
            }
            if(doInference(convertBmp(getBitmap(contentResolver, Uri.parse(photoList[i]))!!)!!) == "HDPE"){
                HDPE += 1
            }
            if(doInference(convertBmp(getBitmap(contentResolver, Uri.parse(photoList[i]))!!)!!) == "Other"){
                Other += 1
            }
        }

        Log.d("photolistsize", photoList.size.toString())

        binding.tvPET.text = PET.toString()
        binding.tvHDPE.text = HDPE.toString()
        binding.tvOTHERS.text = Other.toString()
        
      binding.confirmButton.setOnClickListener{
        val intent = Intent(this, ConfirmationActivity::class.java)
        startActivity(intent)
        }
    }

    private fun doInference(bmp: Bitmap) : String{
        val scaledBmp = Bitmap.createScaledBitmap(bmp, imgSize, imgSize, false)
        return classifyImage(scaledBmp)
    }

    private fun classifyImage(image: Bitmap) : String {
        try {
            val model = Model2.newInstance(applicationContext)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imgSize * imgSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imgSize * imgSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (i in 0 until imgSize) {
                for (j in 0 until imgSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
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
//
//            // Releases model resources if no longer used.

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