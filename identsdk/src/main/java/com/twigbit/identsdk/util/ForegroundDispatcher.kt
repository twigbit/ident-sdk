/*
 * Copyright (c) 2018. Moritz Morgenroth, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Moritz Morgenroth <development@moritzmorgenroth.de>
 */

package com.twigbit.identsdk.util

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep

class ForegroundDispatcher(private val mActivity: Activity) {
    private val mAdapter: NfcAdapter?
    private val mPendingIntent: PendingIntent
    private val mFilters: Array<IntentFilter>
    private val mTechLists: Array<Array<String>>

    init {
        mAdapter = NfcAdapter.getDefaultAdapter(mActivity)
        val intent = Intent(mActivity, mActivity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mPendingIntent = PendingIntent.getActivity(mActivity, 0, intent, 0)

        mFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))
        mTechLists = arrayOf(arrayOf(IsoDep::class.java.name))
    }

    fun enable() {
        mAdapter?.enableForegroundDispatch(mActivity,
                mPendingIntent,
                mFilters,
                mTechLists)
    }

    fun disable() {
        mAdapter?.disableForegroundDispatch(mActivity)
    }
}
