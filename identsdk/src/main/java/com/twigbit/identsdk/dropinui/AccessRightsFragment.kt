package com.twigbit.identsdk.dropinui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.twigbit.identsdk.R
import com.twigbit.identsdk.core.CertificateInfo
import com.twigbit.identsdk.util.Tags
import kotlinx.android.synthetic.main.activity_dropin_identification.*
import kotlinx.android.synthetic.main.fragment_access_rights.view.*
import kotlinx.android.synthetic.main.fragment_access_rights.view.textPurpose
import kotlinx.android.synthetic.main.fragment_access_rights.view.textServiceProvider
import kotlinx.android.synthetic.main.holder_access_right.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AccessRightsFragment : Fragment() {

    var certificateInfo: CertificateInfo? = null
        set(value) {
            field = value
            view?.let { it.textServiceProvider.text = value?.subjectName; it.textPurpose.text = value?.purpose }
        }

    var accessRights: ArrayList<String> = arrayListOf()
        set(value) {
            field = value
            Log.d(Tags.TAG_IDENT_DEBUG, accessRights.toString())
            adapter.data = value
            adapter.notifyDataSetChanged()
        }
    val adapter = MyAdapter(accessRights)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_access_rights, container, false)
        v.buttonAccept.setOnClickListener {
            activity?.asIdentificationUI()?.identificationManager?.acceptAccessRights()
            activity?.asIdentificationUI()?.showLoader()
        }
        v.cardServiceProvider.setOnClickListener {
            activity?.asIdentificationUI()?.showCertificate()
        }
        v.buttonDeny.setOnClickListener {
            activity?.asIdentificationUI()?.identificationManager?.cancel()
            activity?.finish()
        }
        v.recyclerView.adapter = adapter
        if (certificateInfo != null) {
            v.textServiceProvider?.text = certificateInfo?.subjectName
            v.textPurpose?.text = certificateInfo?.purpose
        }
        return v
    }
}

// TODO display the certificate information

class MyAdapter(var data: ArrayList<String>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_access_right, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == data.size - 1) holder.view.divider.visibility = View.GONE
        holder.view.text.text = data.get(position)
    }

    override fun getItemCount() = data.size
}