package com.example.movieapplication.ui.search.not_logged_in_dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapplication.R
import com.example.movieapplication.base.SlidableFragment
import kotlinx.android.synthetic.main.fragment_not_logged_in.*

class NotLoggedInDialogFragment : Fragment(), SlidableFragment {

    private var listener: NotLoggedInDialogFragmentListener? = null

    interface NotLoggedInDialogFragmentListener {
        fun goToLoginScreen()
        fun onCancel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_not_logged_in, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as NotLoggedInDialogFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_ok.setOnClickListener {
            listener?.goToLoginScreen()
        }
        btn_cancel.setOnClickListener {
            listener?.onCancel()
        }
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_bottom
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_bottom
    }

    override fun getFragment(): Fragment {
        return this
    }
}