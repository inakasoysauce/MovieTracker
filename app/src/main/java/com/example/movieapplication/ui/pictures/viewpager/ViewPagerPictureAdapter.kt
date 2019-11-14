package com.example.movieapplication.ui.pictures.viewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseAdapterListener
import com.squareup.picasso.Picasso

class ViewPagerPictureAdapter(private val listener: BaseAdapterListener) : PagerAdapter() {

    private val urlList = ArrayList<String>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context)
            .inflate(R.layout.vp_picture_item, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.vp_picture)
        loadInto(imageView, position)
        container.addView(itemView)
        return itemView

    }

    private fun loadInto(imageView: ImageView, position: Int) {
        Picasso.get()
            .load(urlList[position])
            .into(imageView)
    }


    override fun getCount(): Int {
        return urlList.size
    }

    fun addAll(list: ArrayList<String>?) {
        list?.let {
            urlList.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}