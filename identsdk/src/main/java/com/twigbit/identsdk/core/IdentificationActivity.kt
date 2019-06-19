/*
 * Copyright (c) 2018. Moritz Morgenroth- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Moritz Morgenroth <development@moritzmorgenroth.de>
 */

package com.twigbit.identsdk.core

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import com.twigbit.identsdk.util.NfcInterceptorActivity

abstract class IdentificationActivity : NfcInterceptorActivity() {
    var identificationManager = IdentificationManager()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val tag = intent!!.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            identificationManager.dispatchNfcTag(tag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        identificationManager.bind(applicationContext)

    }
    override fun onStart() {
        super.onStart()
//        identificationManager.bind(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        identificationManager.unBind(applicationContext)
    }
    override fun onStop() {
        super.onStop()
//        identificationManager.unBind(applicationContext)
    }

}
