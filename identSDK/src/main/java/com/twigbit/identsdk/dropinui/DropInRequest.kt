package com.twigbit.identsdk.dropinui

import android.content.Context
import android.content.Intent

class DropInRequest(private val clientToken: String){
    val EXTRA_CLIENT_TOKEN = "twigbit-ident-extra-client-token"
    fun getIntent(context: Context): Intent{
        val intent = Intent(context, DropinIdentificationActivity::class.java)
        intent.putExtra(EXTRA_CLIENT_TOKEN, clientToken)
        return intent
    }
}