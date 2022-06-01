package com.chesia.bangkitcapstoneproject

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chesia.bangkitcapstoneproject.databinding.ActivityTfliteBinding
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class TFliteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTfliteBinding
    private lateinit var tflite : Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTfliteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bmp: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pet2)

        doInference(bmp)

    }

    private fun doInference(bmp: Bitmap){
        val input: Bitmap = bmp
        val output: String = ""

        tflite = Interpreter(loadModelFile())

        tflite.run(input, output);

        val finaloutput: String = output;
        binding.tvModelresult.text = finaloutput

    }

    /** Memory-map the model file in Assets.  */
    @Throws(IOException::class)
    private fun loadModelFile(): ByteBuffer {
        val fileDescriptor = this.assets.openFd("model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}