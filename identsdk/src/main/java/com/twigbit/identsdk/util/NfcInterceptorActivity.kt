/*
 * Copyright (c) 2018. Moritz Morgenroth- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Moritz Morgenroth <development@moritzmorgenroth.de>
 */

package com.twigbit.identsdk.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/*

Additional layer for NFC dispatcher that allows manifest configuration for NFC Intent filter

This does not appear to be necessary. Should be implemented for good measure later though.

Elsewise tell Governikus to move from their docs.

Currently Unused

// TODO: Check under which conditions the NFC filter maifest configuration is needed. Maybe unnecessarry in activities filterin for other intents?

 */
open class NfcInterceptorActivity : AppCompatActivity() {
    var mDispatcher: ForegroundDispatcher? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDispatcher = ForegroundDispatcher(this)
    }

    public override fun onResume() {
        super.onResume()
        mDispatcher!!.enable()
    }

    public override fun onPause() {
        super.onPause()
        mDispatcher!!.disable()
    }
}