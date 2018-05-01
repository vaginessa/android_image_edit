package com.example.lbg99.andrapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.bitmap
import android.annotation.TargetApi
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import java.io.File
import java.io.IOException
import android.os.Environment.DIRECTORY_PICTURES
import android.support.v4.content.FileProvider
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 0
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
    var mCurrentPhotoPath: String? = null
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val imageFileName="mypic"
        val storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cameraBtn.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile();
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
        galeryBtn.setOnClickListener {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(mCurrentPhotoPath)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            this.sendBroadcast(mediaScanIntent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap? = null
        when(requestCode) {
            REQUEST_TAKE_PHOTO -> {
                if(resultCode == Activity.RESULT_OK && data != null) {
                    photoImageView.setImageBitmap(data.extras.get("data") as Bitmap)
                }
            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode === Activity.RESULT_OK) {
                    val selectedImage = data?.getData()
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    photoImageView.setImageBitmap(bitmap)
                }
            }
            else ->  {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }


}