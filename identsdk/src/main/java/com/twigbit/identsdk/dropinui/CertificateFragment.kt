package com.twigbit.identsdk.dropinui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.twigbit.identsdk.R
import kotlinx.android.synthetic.main.activity_dropin_identification.*
import kotlinx.android.synthetic.main.fragment_certificate.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// {"description":{"issuerName":"D-Trust GmbH","issuerUrl":"http://www.d-trust.net","purpose":"AusweisIDent - Online Ausweis Identifizierungsservice der Bundesdruckerei GmbH","subjectName":"Bundesdruckerei GmbH","subjectUrl":"https://ref-ausweisident.eid-service.de","termsOfUsage":"Name, Anschrift und E-Mail-Adresse des Diensteanbieters:\r\nBundesdruckerei GmbH\r\nOlaf Clemens\r\nKommandantenstraße 18\r\n10969 Berlin\r\nsupport@bdr.de\r\n\r\nGeschäftszweck:\r\nAusweisIDent - Online Ausweis Identifizierungsservice der Bundesdruckerei GmbH\r\n\r\nHinweis auf die für den Diensteanbieter zuständigen Stellen, die die Einhaltung der Vorschriften zum Datenschutz kontrollieren:\r\nDie Bundesbeauftragte für den Datenschutz und die Informationsfreiheit\r\nHusarenstraße 30\r\n53117 Bonn\r\n+49 (0)228 997799-0\r\npoststelle@bfdi.bund.de\r\n"},"msg":"CERTIFICATE","validity":{"effectiveDate":"2019-06-24","expirationDate":"2019-06-25"}}

/**
 * A simple [Fragment] subclass.
 *
 */
class CertificateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as DropInIdentificationActivity).imageView.visibility = View.GONE
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_certificate, container, false)
        v.buttonBack.setOnClickListener { activity!!.supportFragmentManager.popBackStack() }
        return v;
    }


}
