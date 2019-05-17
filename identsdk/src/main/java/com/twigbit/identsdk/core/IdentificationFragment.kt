package com.twigbit.identsdk.core

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import java.lang.Exception

class IdentificationFragment: Fragment(){

    val TAG = "com.twigbit.identsdk.core.IdentificationFragment"

    var identificationManager = IdentificationManager()

    override fun onStart() {
        super.onStart()
        context?.let { identificationManager.bind(it) }
    }

    override fun onStop() {
        super.onStop()
        context?.let { identificationManager.unBind(it) }
    }
    companion object{

    }
    fun newInstance(activity: AppCompatActivity) : IdentificationFragment{

        // triy to get the fragment instance if it is already attached
        val fm = activity.supportFragmentManager
        var identificationFragment: IdentificationFragment? = fm.findFragmentByTag(TAG) as IdentificationFragment
        if (identificationFragment == null) {
            identificationFragment = IdentificationFragment()
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        fm.beginTransaction().add(identificationFragment, TAG).commitNow()
                    } catch (e: IllegalStateException) {
                        fm.beginTransaction().add(identificationFragment, TAG).commit()
                        try {
                            fm.executePendingTransactions()
                        } catch (ignored: IllegalStateException) {
                        }

                    } catch (e: NullPointerException) {
                        fm.beginTransaction().add(identificationFragment, TAG).commit()
                        try {
                            fm.executePendingTransactions()
                        } catch (ignored: IllegalStateException) {
                        }
                    }

                } else {
                    fm.beginTransaction().add(identificationFragment, TAG).commit()
                    try {
                        fm.executePendingTransactions()
                    } catch (ignored: IllegalStateException) {
                    }

                }
            } catch (e: IllegalStateException) {
                throw Exception(e.message)
            }

        }
        return identificationFragment
    }
}