package com.twigbit.identsdk.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twigbit.identsdk.IdentificationActivity
import com.twigbit.identsdk.IdentificationManager
import com.twigbit.identsdk.Message
import com.twigbit.identsdk.R

class DropinIdentificationActivity : IdentificationActivity() {
    override fun onStateChanged(state: String, data: String?) {
        runOnUiThread {
            when (state) {
                IdentificationManager.STATE_COMPLETE -> {
                    // The identification was complete, display a success message to the user and fetch the identification result from the server
                }
                IdentificationManager.STATE_ACCESSRIGHTS -> {
                    // A list of the id-card fields that the sdk is trying to access has arrived. Display them to the user and await his confirmation.
                    // TODO continue with runIdent()
                    // TODO better parameter typing
                }
                IdentificationManager.STATE_CARD_INSERTED -> {
                    // A card was attached to the NFC reader
                    // TODO show empty card and detach data.
                }
                IdentificationManager.STATE_ENTER_PIN -> {
                    // The id cards PIN was requested. Display a PIN dialog to the user.
                    // To continue the identification process, call identificationManager.setPin(pin: String)
                }
                IdentificationManager.STATE_ENTER_PUK -> {
                    // The id cards PUK was requested. Display a PUK dialog to the user.
                    // To continue the identification process, call identificationManager.setPuk(puk: String)
                }
                IdentificationManager.STATE_ENTER_CAN -> {
                    // The id cards CAN was requested. Display a CAN dialog to the user.
                    // To continue the identification process, call identificationManager.setCan(can: String)
                }
                IdentificationManager.STATE_BAD -> {
                    // Bad state. Display an error/issue dialog to the user.
                    // TODO figure out reasons for bad state, offer solutions
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropin_identification)
    }
}
