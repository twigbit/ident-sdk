package com.twigbit.identsdk.dropinui

import android.content.Context
import android.content.Intent

class DropInRequest(private val clientToken: String, private val redirectUrl: String){
    fun getIntent(context: Context): Intent{
        val intent = Intent(context, DropInIdentificationActivity::class.java)
        intent.putExtra(EXTRA_CLIENT_TOKEN, clientToken)
        intent.putExtra(EXTRA_REDIRECT_URL, redirectUrl)
        return intent
    }

    companion object {
        val EXTRA_CLIENT_TOKEN = "twigbit-ident-extra-client-token"
        val EXTRA_REDIRECT_URL = "twigbit-ident-extra-redirect-url"
    }
}