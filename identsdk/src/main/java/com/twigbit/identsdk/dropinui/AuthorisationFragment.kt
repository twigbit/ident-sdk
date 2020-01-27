package com.twigbit.identsdk.dropinui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.twigbit.identsdk.R
import com.twigbit.identsdk.util.Tags
import kotlinx.android.synthetic.main.fragment_authorisation.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AuthorisationFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_authorisation, container, false)
        v.buttonContinue.setOnClickListener { onEntered(v.editPin.text.toString()) }
        v.editPin.setOnEditorActionListener { v, actionId, event ->
            onEntered(v.text.toString())
            false
        }
        this.mode = arguments?.getInt(KEY_MODE)

        when(mode) {
            MODE_CAN -> {
                v.text?.text = getText(R.string.twigbit_ident_drop_in_enter_can)
            }
            MODE_PIN -> {
                v.text?.text = getText(R.string.twigbit_ident_drop_in_enter_pin)
            }
            MODE_PUK -> {
                v.text?.text = getText(R.string.twigbit_ident_drop_in_enter_puk)
            }
        }
        return v
    }


    override fun onResume() {
        super.onResume()
        view?.editPin?.text = null
    }
    fun onEntered(pin: String){
        // TODO check pin lenght
        Log.d(Tags.TAG_IDENT_DEBUG, "Pin entered $pin");
        when(mode) {
            MODE_CAN -> {
                activity?.asIdentificationUI()?.identificationManager?.setCan(pin)
            }
            MODE_PIN -> {
                activity?.asIdentificationUI()?.identificationManager?.setPin(pin)
            }
            MODE_PUK -> {
                activity?.asIdentificationUI()?.identificationManager?.setPuk(pin)
            }
        }
        activity?.asIdentificationUI()?.showLoader()
    }

    companion object {
        const val MODE_CAN = 0;
        const val MODE_PIN = 1;
        const val MODE_PUK = 2;
        const val KEY_MODE = "key-mode"
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        this.mode = args!!.getInt(KEY_MODE)
    }

    var mode: Int? = MODE_CAN
        set(value) {
            when(value){
                MODE_CAN -> {
                    view?.text?.text = getText(R.string.twigbit_ident_drop_in_enter_can)
                }
                MODE_PIN -> {
                    view?.text?.text = getText(R.string.twigbit_ident_drop_in_enter_pin)
                }
                MODE_PUK -> {
                    view?.text?.text = getText(R.string.twigbit_ident_drop_in_enter_puk)
                }
            }
            field = value
        }
}
