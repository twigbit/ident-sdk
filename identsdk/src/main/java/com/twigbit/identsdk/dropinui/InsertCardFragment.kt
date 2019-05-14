package com.twigbit.identsdk.dropinui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.twigbit.identsdk.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InsertCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InsertCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class InsertCardFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_insert_card, container, false)
        return v
    }
}
