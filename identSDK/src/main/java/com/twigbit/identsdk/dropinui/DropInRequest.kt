package com.twigbit.identsdk.dropinui

import android.content.Context
import android.content.Intent

class DropInRequest(private val clientToken: String){
    fun getIntent(context: Context): Intent{
        val intent = Intent(context, DropInIdentificationActivity::class.java)
        intent.putExtra(Companion.EXTRA_CLIENT_TOKEN, clientToken)
        return intent
    }

    companion object {
        val EXTRA_CLIENT_TOKEN = "twigbit-ident-extra-client-token"
    }
}