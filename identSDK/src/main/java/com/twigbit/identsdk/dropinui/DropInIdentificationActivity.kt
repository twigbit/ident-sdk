package com.twigbit.identsdk.dropinui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.twigbit.identsdk.util.IdentificationActivity
import com.twigbit.identsdk.R
import com.twigbit.identsdk.model.IdentificationCard
import com.twigbit.identsdk.model.IdentificationError
import com.twigbit.identsdk.model.IdentificationManager
import kotlinx.android.synthetic.main.fragment_intro.*

class DropInIdentificationActivity : IdentificationActivity() {
    val introFragment = IntroFragment()
    val loaderFragment = LoaderFragment()
    val accessRightsFragment = AccessRightsFragment()
    val authorisationFragment = AuthorisationFragment()
    val successFragment = SuccessFragment()
    val errorFragment = ErrorFragment()

    val identificationCallback = object: IdentificationManager.Callback{
        override fun onCompleted(resultUrl: String) {
            // The identification was complete, display a success message to the user and fetch the identification result from the server using the resultUrl
        }

        override fun onRequestAccessRights(accessRights: ArrayList<String>) {
            // A list of the fields that the sdk is trying to access has arrived. Display them to the user and await his confirmation.
            // TODO continue with runIdent()
        }

        override fun onCardRecognized(card: IdentificationCard) {
            // A card was attached to the NFC reader
            // TODO @dev implement card model from JSON message params.
        }

        override fun onRequestPin() {
            // The id cards PIN was requested. Display a PIN dialog to the user.
            // To continue the identification process, call identificationManager.setPin(pin: String)
        }

        override fun onRequestPuk() {
            // The id cards PUK was requested. Display a PUK dialog to the user.
            // To continue the identification process, call identificationManager.setPuk(puk: String)
        }

        override fun onRequestCan() {
            // The id cards CAN was requested. Display a CAN dialog to the user.
            // To continue the identification process, call identificationManager.setCan(can: String)
        }

        override fun onError(error: IdentificationError) {
            // An error occured. Display an error/issue dialog to the user.
        }
    }
    fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropin_identification)

        identificationManager.addCallback(identificationCallback)
        //identificationManager.startIdent(intent.getStringExtra(DropInRequest.EXTRA_CLIENT_TOKEN))
        showFragment(introFragment)
    }
}
    /*
    DEPRECATED in favor of IdentificatioCallback
    Refactored into callback architecture. Will be removed soon.
     */
//    override fun onStateChanged(state: String, data: String?) {
//        runOnUiThread {
//            when (state) {
//                IdentificationManager.STATE_COMPLETE -> {
//                    // The identification was complete, display a success message to the user and fetch the identification result from the server
//                }
//                IdentificationManager.STATE_ACCESSRIGHTS -> {
//                    // A list of the id-card fields that the sdk is trying to access has arrived. Display them to the user and await his confirmation.
//                    // TODO continue with runIdent()
//                    // TODO better parameter typing
//                }
//                IdentificationManager.STATE_CARD_INSERTED -> {
//                    // A card was attached to the NFC reader
//                    // TODO show empty card and detach data.
//                }
//                IdentificationManager.STATE_ENTER_PIN -> {
//                    // The id cards PIN was requested. Display a PIN dialog to the user.
////                    // To continue the identification process, call identificationManager.setPin(pin: String)
////                }
////                IdentificationManager.STATE_ENTER_PUK -> {
//                    // The id cards PUK was requested. Display a PUK dialog to the user.
//                    // To continue the identification process, call identificationManager.setPuk(puk: String)
//                }
//                IdentificationManager.STATE_ENTER_CAN -> {
//                    // The id cards CAN was requested. Display a CAN dialog to the user.
//                    // To continue the identification process, call identificationManager.setCan(can: String)
//                }
//                IdentificationManager.STATE_BAD -> {
//                    // Bad state. Display an error/issue dialog to the user.
//                    // TODO figure out reasons for bad state, offer solutions
//                }
//            }
//        }
//    }
