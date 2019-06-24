package com.twigbit.identsdk.dropinui

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.twigbit.identsdk.R
import com.twigbit.identsdk.core.Card
import com.twigbit.identsdk.core.IdentificationActivity
import com.twigbit.identsdk.core.IdentificationManager
import com.twigbit.identsdk.util.*
import kotlinx.android.synthetic.main.fragment_intro.*

fun Activity.asIdentificationUI(): IsIdentificationUI?{
    if(this is IsIdentificationUI) return this else return null
}

class DropInIdentificationActivity : IdentificationActivity(), IsIdentificationUI {

    val introFragment = IntroFragment()
    val loaderFragment = LoaderFragment()
    val accessRightsFragment = AccessRightsFragment()
    val authorisationFragment = AuthorisationFragment()
    val insertCardFragment = InsertCardFragment()
    val successFragment = SuccessFragment()
    val errorFragment = ErrorFragment()
    val certificateFragment = CertificateFragment()

    val identificationCallback = object: IdentificationManager.Callback{
        override fun onCompleted(resultUrl: String) {
            // The identification was complete, display a success message to the user and fetch the identification result from the server using the resultUrl
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onComplete Callback")
            showFragment(successFragment)
        }

        override fun onRequestAccessRights(accessRights: List<String>) {
            // A list of the fields that the sdk is trying to access has arrived. Display them to the user and await his confirmation.
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onRequestAccessRights Callback")

            accessRightsFragment.accessRights = ArrayList(accessRights.map { StringUtil.translate(this@DropInIdentificationActivity, it)})
            // for the moment just accept them
            showFragment(accessRightsFragment)
        }

        override fun onCardRecognized(card: Card?) {
            // A card was attached to the NFC reader
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onCardRecognized Callback")
            showFragment(insertCardFragment)
        }

        override fun onRequestPin() {
            // The id cards PIN was requested. Display a PIN dialog to the user.
            // To continue the identification process, call identificationManager.setPin(pin: String)
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onRequestPin Callback")
            authorisationFragment.mode = AuthorisationFragment.MODE_PIN
            authorisationFragment.arguments = Bundle().apply { putInt(AuthorisationFragment.KEY_MODE, AuthorisationFragment.MODE_PIN) }
            showFragment(authorisationFragment)
        }

        override fun onRequestPuk() {
            // The id cards PUK was requested. Display a PUK diaphlog to the user.
            // To continue the identification process, call identificationManager.setPuk(puk: String)
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onRequestPuk Callback")
            authorisationFragment.mode = AuthorisationFragment.MODE_PUK
            authorisationFragment.arguments = Bundle().apply { putInt(AuthorisationFragment.KEY_MODE, AuthorisationFragment.MODE_PUK) }
            showFragment(authorisationFragment)
        }

        override fun onRequestCan() {
            // The id cards CAN was requested. Display a CAN dialog to the user.
            // To continue the identification process, call identificationManager.setCan(can: String)
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onRequestCan Callback")
            authorisationFragment.mode = AuthorisationFragment.MODE_CAN
            authorisationFragment.arguments = Bundle().apply { putInt(AuthorisationFragment.KEY_MODE, AuthorisationFragment.MODE_CAN) }
            showFragment(authorisationFragment)
        }

        override fun onError(error: String) {
            // An error occured. Display an error/issue dialog to the user.
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onError Callback")
            showFragment(errorFragment)
        }
    }
    fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropin_identification)

        identificationManager.addCallback(identificationCallback)
        showFragment(introFragment)
    }

    override fun startIdent(){
        identificationManager.startIdent(intent.getStringExtra(DropInRequest.EXTRA_TC_TOKEN_URL))
    }


    override fun showLoader() {
        showFragment(loaderFragment);
    }
    override fun showCertificate() {
        // TODO add to back stack instead of just showing
        // TODO request certificate info from sdk, refactor show fragement into callback
        supportFragmentManager.beginTransaction().addToBackStack("").replace(R.id.container, certificateFragment).commit()
    }
}

interface IsIdentificationUI{
    val identificationManager: IdentificationManager?;
    fun showLoader();
    fun startIdent();
    fun showCertificate();
}