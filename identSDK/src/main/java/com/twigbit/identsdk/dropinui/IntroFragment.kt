package com.twigbit.identsdk.dropinui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.twigbit.identsdk.R
import kotlinx.android.synthetic.main.fragment_intro.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class IntroFragment : Fragment() {

    fun getDropInActivity(): DropInIdentificationActivity{
        return activity as DropInIdentificationActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_intro, container, false)
        v.buttonStart.setOnClickListener {
            (getDropInActivity()).showFragment(getDropInActivity().loaderFragment)
        }
        return v;
    }


}
