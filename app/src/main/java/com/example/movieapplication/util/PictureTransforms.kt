package com.example.movieapplication.util

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.widget.ImageView

object PictureTransforms {

    fun resizePicture(bitmap: Bitmap, newWidth: Float, newHeight: Float) : Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = newWidth / width
        val scaleHeight = newHeight / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
        bitmap.recycle()
        return resizedBitmap
    }

    fun setColored(imageView: ImageView) {
        setColorFilter(imageView, 1F)
    }

    fun setColorless(imageView: ImageView) {
        setColorFilter(imageView, 0F)
    }

    private fun setColorFilter(imageView: ImageView, value: Float) {
        val matrix = ColorMatrix()
        matrix.setSaturation(value)
        val filter = ColorMatrixColorFilter(matrix)
        imageView.colorFilter = filter
    }
}