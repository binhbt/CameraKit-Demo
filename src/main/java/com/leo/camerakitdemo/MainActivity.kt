package com.leo.camerakitdemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.camerakit.CameraKitView.ImageCallback
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_capture.setOnClickListener {
            Log.e("btn_capture", "Capture Image")
            camera.captureImage(ImageCallback { cameraKitView, capturedImage ->
                val savedPhoto =
                    File(Environment.getExternalStorageDirectory(), "photo.jpg")
                try {
                    val outputStream = FileOutputStream(savedPhoto.getPath())
                    outputStream.write(capturedImage)
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val squarePath = saveSquareImage(savedPhoto.getPath(), 512)
                if (squarePath!=null) {
                    displayImage(squarePath)
                }
            })
        }
    }

    fun saveSquareImage(path: String, width: Int):String?{
        val imageStream  = getContentResolver().openInputStream(Uri.fromFile(File(path)))
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        val resizedImage = Bitmap.createScaledBitmap(selectedImage, width, width, false)

        try {
            val savedPhoto =
                File(Environment.getExternalStorageDirectory(), "square.jpg")
            FileOutputStream(savedPhoto.getPath()).use({ out ->
                resizedImage.compress(Bitmap.CompressFormat.JPEG, 60, out) // bmp is your Bitmap instance
            })
            return savedPhoto.path
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    fun displayImage(path: String){
        val imageStream  = getContentResolver().openInputStream(Uri.fromFile(File(path)))

        val selectedImage = BitmapFactory.decodeStream(imageStream)
        if(selectedImage != null) {
            Log.e("width", selectedImage.width.toString())
            Log.e("height", selectedImage.height.toString())
            image.setImageBitmap(selectedImage)
        }


    }

    override fun onStart() {
        super.onStart()
        camera.onStart()
    }

    override fun onResume() {
        super.onResume()
        camera.onResume()
    }

    override fun onPause() {
        camera.onPause()
        super.onPause()
    }

    override fun onStop() {
        camera.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults!!)
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
