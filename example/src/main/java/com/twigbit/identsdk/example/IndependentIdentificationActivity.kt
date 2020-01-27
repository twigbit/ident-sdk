package com.twigbit.identsdk.example

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import com.twigbit.identsdk.ausweisident.AusweisIdentBuilder
import com.twigbit.identsdk.ausweisident.AusweisIdentScopes
import com.twigbit.identsdk.core.*
import com.twigbit.identsdk.dropinui.*
import com.twigbit.identsdk.util.ForegroundDispatcher
import com.twigbit.identsdk.util.StringUtil
import com.twigbit.identsdk.util.Tags

class IndependentIdentificationActivity : AppCompatActivity(), IsIdentificationUI {

    override fun startIdent() {
        val tcTokenUrl = AusweisIdentBuilder()
            .ref()
            .clientId(Secrets.CLIENT_ID)
            .redirectUrl(Secrets.CLIENT_REDIRECT_URL)
            .scope(AusweisIdentScopes.FAMILY_NAMES)
            .scope(AusweisIdentScopes.GIVEN_NAMES)
            .scope(AusweisIdentScopes.DATE_OF_BIRTH)
            .state("123456")
            .build()
        identificationFragment?.identificationManager?.startIdent(tcTokenUrl);
    }
    override fun showLoader() {
        showFragment(loaderFragment)
    }

    var identificationFragment: IdentificationFragment? = null
    // convenience getter
    override val identificationManager: IdentificationManager?
        get() {
            return identificationFragment?.identificationManager
        }
    /*
    To receive and dispatch NFC tags to the SDK, we need to initalize a forground dispatcher and attach it to the lifecycle.
    Then, we can pass Tag Intents from the `onNewIntent` to the `Identificationmanager`
     */

    var mDispatcher: ForegroundDispatcher? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_independent_identification)


        identificationFragment = IdentificationFragment.newInstance(this)
        identificationFragment!!.identificationManager.addCallback(identificationCallback)

        // Initialize the NFC Tag foreground dispatcher
        mDispatcher = ForegroundDispatcher(this)

        showFragment(introFragment)
    }
    public override fun onResume() {
        super.onResume()
        mDispatcher!!.enable()
    }

    public override fun onPause() {
        super.onPause()
        mDispatcher!!.disable()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val tag = intent!!.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {
            identificationManager?.dispatchNfcTag(tag)
        }
    }

    /*
    These are the same fragments used by the drop in UI.
    For a custom UI implementation, you could implement your own fragments with your own UI components
     */

    val introFragment = IntroFragment()
    val loaderFragment = LoaderFragment()
    val accessRightsFragment = AccessRightsFragment()
    val authorisationFragment = AuthorisationFragment()
    val insertCardFragment = InsertCardFragment()
    val successFragment = SuccessFragment()
    val certificateFragment = CertificateFragment()
    val errorFragment = ErrorFragment()

    val identificationCallback = object: IdentificationManager.Callback{
        override fun onRequestCertificate(certificateInfo: CertificateInfo, certificateValidity: CertificateValidity) {
            // The certificate info has beed requested and is delivered here
            certificateFragment.certificateInfo = certificateInfo;
            certificateFragment.certificateValidity = certificateValidity;
            accessRightsFragment.certificateInfo = certificateInfo;
        }

        override fun onCompleted(resultUrl: String) {
            // The identification was complete, display a success message to the user and fetch the identification result from the server using the resultUrl
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onComplete Callback")
            showFragment(successFragment)
        }

        override fun onRequestAccessRights(accessRights: List<String>) {
            // A list of the fields that the sdk is trying to access has arrived. Display them to the user and await his confirmation.
            Log.d(Tags.TAG_IDENT_DEBUG, "Got onRequestAccessRights Callback")

            identificationManager?.getCertificate();

            accessRightsFragment.accessRights = ArrayList(accessRights.map { StringUtil.translate(this@IndependentIdentificationActivity, it)})
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
    override fun showCertificate() {
        supportFragmentManager.beginTransaction().addToBackStack("").replace(com.twigbit.identsdk.R.id.container, certificateFragment).commit()
    }

    fun showFragment(fragment: androidx.fragment.app.Fragment){
        supportFragmentManager.beginTransaction().replace(com.twigbit.identsdk.R.id.container, fragment).commit()
    }

}
