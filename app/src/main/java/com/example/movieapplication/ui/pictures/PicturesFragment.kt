package com.example.movieapplication.ui.pictures

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import com.example.movieapplication.ui.pictures.recycler_view.RecyclerViewPictureAdapter
import com.example.movieapplication.ui.pictures.viewpager.PictureTransformer
import com.example.movieapplication.ui.pictures.viewpager.ViewPagerPictureAdapter
import com.example.movieapplication.util.PictureClickedListener
import kotlinx.android.synthetic.main.fragment_pictures.*

class PicturesFragment : BaseFragment<PicturesPresenter, PicturesView>(), PicturesView, PictureClickedListener, ViewPager.OnPageChangeListener {

    private var rvAdapter: RecyclerViewPictureAdapter? = null
    private var vpAdapter: ViewPagerPictureAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponents()
        presenter?.getPictures(arguments?.getString("id"), arguments?.getString("type"))
        showLoading()
    }

    override fun initComponents() {
        initRecyclerView()
        initViewPager()
    }

    private fun initRecyclerView() {
        rvAdapter = RecyclerViewPictureAdapter(this)
        rv_pictures.adapter = rvAdapter
        rv_pictures.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initViewPager() {
        vpAdapter = ViewPagerPictureAdapter(this)
        vp_pictures.adapter = vpAdapter
        vp_pictures.setPageTransformer(true, PictureTransformer(vp_pictures))
        vp_pictures.addOnPageChangeListener(this)
    }

    override fun loadImages(picturesList: ArrayList<String>?) {
        hideLoading()
        rvAdapter?.addAll(picturesList)
        vpAdapter?.addAll(picturesList)
        vpAdapter?.notifyDataSetChanged()
        showPicture(1)
    }

    override fun showPicture(position: Int) {
        vp_pictures.setCurrentItem(position, true)
    }

    override fun onPageSelected(position: Int) {
        rv_pictures.smoothScrollToPosition(position)
        rvAdapter?.setSelectedPicture(position)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun getViewContext(): Context {
        return context!!
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_right
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_right
    }

    override fun createPresenter(): PicturesPresenter {
        return PicturesPresenter(this)
    }
}