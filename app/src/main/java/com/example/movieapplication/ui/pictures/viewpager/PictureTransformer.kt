package com.example.movieapplication.ui.pictures.viewpager

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max

class PictureTransformer(private val viewPager: ViewPager) : ViewPager.PageTransformer {


    companion object {

        private const val MIN_ALPHA = 0.6f
        private const val MIN_SCALE = 0.5f
    }

    override fun transformPage(page: View, position: Float) {
        var pos = position
        val pageWidth = page.width
        val margin = viewPager.paddingStart.toFloat() / pageWidth
        pos -= margin
        val pageHeight = page.height
        val alphaFactor = max(MIN_SCALE, 1 - abs(pos))
        val scaleFactor = max(MIN_SCALE, 1 - abs(pos / 4))
        val verticalMargin = pageHeight * (1 - scaleFactor) / 2
        val horizontalMargin = pageWidth * (1 - scaleFactor) / 1.5f
        if (pos < 0) {
            page.translationX = horizontalMargin - verticalMargin
        } else {
            page.translationX = -horizontalMargin + verticalMargin
        }
        page.translationY = verticalMargin / 1.5f
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor
        page.alpha = MIN_ALPHA + (alphaFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
    }
}