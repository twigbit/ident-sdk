package com.twigbit.identsdk.example

import android.os.Bundle
import com.twigbit.identsdk.dropinui.DropInRequest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.twigbit.identsdk.ausweisident.AusweisIdentBuilder
import com.twigbit.identsdk.ausweisident.AusweisIdentResultHandler
import com.twigbit.identsdk.ausweisident.AusweisIdentScopes
import com.twigbit.identsdk.ausweisident.UserInfo
import com.twigbit.identsdk.core.IdentificationManager
import com.twigbit.identsdk.util.Tags
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonDropIn.setOnClickListener {
            startDropInIdentification()
        }
        buttonCustomAuth.setOnClickListener {
            startIdentificationActivity()
        }
    }

    private fun startIdentificationActivity() {
        val intent = Intent(this, IndependentIdentificationActivity::class.java)
        startActivity(intent)
    }

    val REQUEST_CODE_IDENTIFICATION = 0;
    private fun startDropInIdentification() {
        val tcTokenUrl = AusweisIdentBuilder()
            .ref()
            .clientId(Secrets.CLIENT_ID)
            .redirectUrl(Secrets.CLIENT_REDIRECT_URL)
            .scope(AusweisIdentScopes.FAMILY_NAMES)
            .scope(AusweisIdentScopes.GIVEN_NAMES)
            .scope(AusweisIdentScopes.DATE_OF_BIRTH)
            .state("123456")
            .build()

        val dropInRequest = DropInRequest(tcTokenUrl)
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE_IDENTIFICATION)
    }

    val resultHandler: AusweisIdentResultHandler =
        AusweisIdentResultHandler(object : AusweisIdentResultHandler.Callback {
            override fun onError(message: String) {
                Log.d(Tags.TAG_IDENT_DEBUG, "An error occured")
            }

            override fun onComplete(userInfo: UserInfo) {
                Log.d(Tags.TAG_IDENT_DEBUG, userInfo.toString())
            }
        })

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_IDENTIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                // Success. Update the UI to reflect the successful identification
                // and fetch the user data from the server where they were delivered.

                val resultUrl = data!!.getStringExtra(IdentificationManager.EXTRA_DROPIN_RESULT)
                Log.d(Tags.TAG_IDENT_DEBUG, resultUrl);

                resultHandler.fetchResult(resultUrl);
                // to deliver the data to the server, call this URL and follow the redirect chain

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the identification
            } else {
                // An error occured during the identification
            }
        }
    }
}
