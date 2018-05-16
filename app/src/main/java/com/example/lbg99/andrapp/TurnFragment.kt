package com.example.lbg99.andrapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_turn.*
import kotlinx.android.synthetic.main.fragment_unsharp_masking.*

class TurnFragment : Fragment() {

    var tmpImage : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_turn, container, false)
    }
    override fun onStart() {
        super.onStart()
        cancelTurnBtn.setOnClickListener {
            turnView.setImageBitmap(commonData.imageBitmap)
            tmpImage = commonData.imageBitmap
            turnSeek.progress = 0
        }

        applyTurnBtn.setOnClickListener {
            commonData.imageBitmap = tmpImage
        }
        turnView.setImageBitmap(commonData.imageBitmap)
        tmpImage = commonData.imageBitmap

        turnSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                degreeValue.text =  turnSeek.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                tmpImage = Rotate(turnSeek.progress.toDouble(), commonData.imageBitmap)
                turnView.setImageBitmap(tmpImage)
            }
        })
    }
    fun Rotate(value: Double, bitmap: Bitmap?) : Bitmap {
        var value = value
        value = Math.toRadians(value)
        //инициализация переменных перед преобазованием
        val w = bitmap!!.width
        val h = bitmap!!.height
        val pixels = IntArray(w * h)
        bitmap!!.getPixels(pixels, 0, w, 0, 0, w, h)
        //поворот currentBitmap
        val tmp = rotate(value, w, h, pixels)
        return tmp
    }

    /** повораичвает изображение на заданный угол  */
    //сейчас использует встроенные средства; возможно, это надо исправить
    fun rotate(alpha: Double, w: Int, h: Int, pixels: IntArray): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(Math.toDegrees(alpha).toFloat())
        var temp = Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888)
        temp = Bitmap.createBitmap(temp, 0, 0, w, h, matrix, true)
        return temp
    }
    override fun onStop() {
        super.onStop()
    }
}