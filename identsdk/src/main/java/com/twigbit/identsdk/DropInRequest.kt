package com.twigbit.identsdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.twigbit.identsdk.ui.DropinIdentificationActivity

class DropInRequest(private val clientToken: String){
    val EXTRA_CLIENT_TOKEN = "twigbit-ident-extra-client-token"
    fun getIntent(context: Context): Intent{
        val intent = Intent(context, DropinIdentificationActivity::class.java)
        intent.putExtra(EXTRA_CLIENT_TOKEN, clientToken)
        return intent
    }
}