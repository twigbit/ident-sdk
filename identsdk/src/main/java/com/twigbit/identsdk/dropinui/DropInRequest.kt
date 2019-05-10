package com.twigbit.identsdk.dropinui

import android.content.Context
import android.content.Intent

class DropInRequest(private val tcTokenUrl: String){
    fun getIntent(context: Context): Intent{
        val intent = Intent(context, DropInIdentificationActivity::class.java)
        intent.putExtra(EXTRA_TC_TOKEN_URL, tcTokenUrl)
        return intent
    }

    companion object {
        val EXTRA_TC_TOKEN_URL = "twigbit-ident-extra-tc-token-url"
    }
}