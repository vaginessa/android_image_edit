package com.example.lbg99.andrapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : Fragment() {

    val TAG = "fragmentFilters"
    var curPath: String? = null
    private var tmpImage: Bitmap? = null
    private var oldPixels: Array<IntArray>? = null
    private var pixels: Array<IntArray>? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        curPath = commonData.currentPhotoPath
        tmpImage = commonData.imageBitmap
        photoView.setImageBitmap(tmpImage)
        pixels = commonData.pixels
        oldPixels = pixels

        cancelFilterBtn.setOnClickListener {
            pixels = oldPixels
            tmpImage = commonData.imageBitmap
            photoView.setImageBitmap(tmpImage)
        }

        sepiaBtn.setOnClickListener {
            tmpImage = sepia(pixels)
            photoView.setImageBitmap(tmpImage)
        }

        binBtn.setOnClickListener {
            tmpImage = bin(pixels)
            photoView.setImageBitmap(tmpImage)
        }

        inverseBtn.setOnClickListener {
            tmpImage = inverse(pixels)
            photoView.setImageBitmap(tmpImage)
        }

        grayBtn.setOnClickListener {
            tmpImage = gray(pixels)
            photoView.setImageBitmap(tmpImage)
        }

        applyFilterBtn.setOnClickListener {
            commonData.imageBitmap = tmpImage
            commonData.pixels = pixels
        }
    }

    fun bin(pxl : Array<IntArray>?) : Bitmap? {
        var matrix = pxl
        for (i in 0 until tmpImage!!.width)
            for(j in 0 until tmpImage!!.height)
            {
                val color = matrix!![i][j]
                val r = Color.red(color)
                val g = Color.green(color)
                val b = Color.blue(color)
                val luminance = 0.299 * r + 0.0 + 0.587 * g + 0.0 + 0.114 * b + 0.0
                matrix[i][j] = if (luminance > 125) Color.WHITE else Color.BLACK
            }
        var tmp: Bitmap? = Bitmap.createBitmap(tmpImage!!.width, tmpImage!!.height, Bitmap.Config.RGB_565)
        for(i in 0 until matrix!!.size)
            for(j in 0 until matrix[i].size)
                tmp!!.setPixel(i,j,matrix[i][j])
        pixels = matrix
        return tmp
    }

    fun inverse(pxl : Array<IntArray>?) : Bitmap? {
        var matrix = pxl
        for (i in 0 until tmpImage!!.width)
            for(j in 0 until tmpImage!!.height)
            {
                val color = matrix!![i][j]
                val r = 255 - Color.red(color)
                val g = 255 - Color.green(color)
                val b = 255 - Color.blue(color)
                val a = 255
                val p = a shl 24 or (r shl 16) or (g shl 8) or b
                matrix[i][j] = p
            }
        var tmp: Bitmap? = Bitmap.createBitmap(tmpImage!!.width, tmpImage!!.height, Bitmap.Config.RGB_565)
        for(i in 0 until matrix!!.size)
            for(j in 0 until matrix[i].size)
                tmp!!.setPixel(i,j,matrix[i][j])
        pixels = matrix
        return tmp
    }

    fun gray(pxl : Array<IntArray>?) : Bitmap? {
        var r: Int
        var g: Int
        var b: Int
        var Y: Double
        var I: Double
        var Q: Double
        var matrix = pxl
        for (i in 0 until tmpImage!!.width)
            for (j in 0 until tmpImage!!.height) {
                val color = matrix!![i][j]
                r = Color.red(color)
                g = Color.green(color)
                b = Color.blue(color)
                Y = 0.299 * r + 0.587 * g + 0.114 * b
                I = 1.0
                Q = 0.0
                //Transform to RGB
                r = (1.0 * Y + 0.999 * I + 0.621 * Q).toInt()
                g = (1.0 * Y - 0.272 * I - 0.647 * Q).toInt()
                b = (1.0 * Y - 1.105 * I + 1.702 * Q).toInt()
                //Fix values
                r = if (r < 0) 0 else r
                r = if (r > 255) 255 else r
                g = if (g < 0) 0 else g
                g = if (g > 255) 255 else g
                b = if (b < 0) 0 else b
                b = if (b > 255) 255 else b
                val a = 255
                val p = a shl 24 or (r shl 16) or (g shl 8) or b
                matrix[i][j] = p
            }
        var tmp: Bitmap? = Bitmap.createBitmap(tmpImage!!.width, tmpImage!!.height, Bitmap.Config.RGB_565)
        for(i in 0 until matrix!!.size)
            for(j in 0 until matrix[i].size)
                tmp!!.setPixel(i,j,matrix[i][j])
        pixels = matrix
        return tmp
    }

    fun sepia(pxl : Array<IntArray>?) : Bitmap? {
        var r: Int
        var g: Int
        var b: Int
        var Y: Double
        var I: Double
        var Q: Double
        var matrix = pxl
        for (i in 0 until tmpImage!!.width)
            for(j in 0 until tmpImage!!.height)
            {
                val color = matrix!![i][j]
                r = Color.red(color)
                g = Color.green(color)
                b = Color.blue(color)
                Y = 0.299 * r + 0.587 * g + 0.114 * b
                I = 51.0
                Q = 0.0
                //Transform to RGB
                r = (1.0 * Y + 0.999 * I + 0.621 * Q).toInt()
                g = (1.0 * Y - 0.272 * I - 0.647 * Q).toInt()
                b = (1.0 * Y - 1.105 * I + 1.702 * Q).toInt()
                //Fix values
                r = if (r < 0) 0 else r
                r = if (r > 255) 255 else r
                g = if (g < 0) 0 else g
                g = if (g > 255) 255 else g
                b = if (b < 0) 0 else b
                b = if (b > 255) 255 else b
                val a = 255
                val p = a shl 24 or (r shl 16) or (g shl 8) or b
                matrix[i][j] = p
            }
        var tmp: Bitmap? = Bitmap.createBitmap(tmpImage!!.width, tmpImage!!.height, Bitmap.Config.RGB_565)
        for(i in 0 until matrix!!.size)
            for(j in 0 until matrix[i].size)
                tmp!!.setPixel(i,j,matrix[i][j])
        pixels = matrix
        return tmp
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
    }
}