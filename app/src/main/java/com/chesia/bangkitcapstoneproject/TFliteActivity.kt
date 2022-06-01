package com.chesia.bangkitcapstoneproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chesia.bangkitcapstoneproject.databinding.ActivityTfliteBinding
import com.chesia.bangkitcapstoneproject.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


class TFliteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTfliteBinding
    private var imgSize: Int = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTfliteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bmp: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pet2)
        binding.imgPhotoresult.setImageBitmap(bmp);

        doInference(bmp)

    }

    private fun doInference(bmp: Bitmap){
        val scaledBmp = Bitmap.createScaledBitmap(bmp, imgSize, imgSize, false)
        classifyImage(scaledBmp)
    }

    fun classifyImage(image: Bitmap) {
        try {
            val model = Model.newInstance(applicationContext)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 200, 200, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imgSize * imgSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imgSize * imgSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (i in 0 until imgSize) {
                for (j in 0 until imgSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255))
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
            binding.tvModelresult.text = classes[maxPos]

            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {

        }
    }
}