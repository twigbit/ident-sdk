/*
 * Copyright (c) 2018. Moritz Morgenroth- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Moritz Morgenroth <development@moritzmorgenroth.de>
 */

package com.twigbit.identsdk.core


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.nfc.Tag
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.governikus.ausweisapp2.IAusweisApp2Sdk
import com.governikus.ausweisapp2.IAusweisApp2SdkCallback
import com.twigbit.identsdk.util.*

class IdentificationManager{
    // TODO refactor into callback array or else replace by setter
    var callback : Callback? = null
    fun addCallback(callback: Callback){
        this.callback = callback
    }

    // Initialize the auth Process via this function
    fun bind(context: Context){
        bindIdIdentificationService(context)
    }
    fun unBind(context: Context){
        context.unbindService(sdkConnection)
    }
    fun startIdent(tokenURL: String) {
        send(
            IdentificationUtil.buildCmdString(
                IdentificationUtil.CMD_RUN_AUTH, Pair(
                    IdentificationUtil.PARAM_TCTOKEN, tokenURL)))
    }
    fun setPin(pin: String){
        send(
            IdentificationUtil.buildCmdString(
                IdentificationUtil.CMD_SET_PIN, Pair(
                    IdentificationUtil.PARAM_VALUE, pin)))
    }
    fun setPuk(puk: String){
        send(
            IdentificationUtil.buildCmdString(
                IdentificationUtil.CMD_SET_PUK, Pair(
                    IdentificationUtil.PARAM_VALUE, puk)))
    }
    fun setCan(can: String){
        send(
            IdentificationUtil.buildCmdString(
                IdentificationUtil.CMD_SET_CAN, Pair(
                    IdentificationUtil.PARAM_VALUE, can)))
    }
    fun acceptAccessRights(){
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_ACCEPT))
    }
    fun cancel(){
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_CANCEL))
    }
    fun getCertificate(){
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_GET_CERTIFICATE))
    }

    private fun send(cmd: String) {
        Log.d(Tags.TAG_IDENT_DEBUG,  "Sending command: " + cmd)
        try {
            if (!sdk!!.send(sdkCallback.mSessionID, cmd)) {
                // TODO error
                Log.e(Tags.TAG_IDENT_DEBUG, "Could not sendRaw command to SDK")
            }
        } catch (e: Exception) {
            // TODO error
            // FIXME 20190509 This sometimes gets called without an error message. Possibly due to improperly terminated background service. 
            Log.e(Tags.TAG_IDENT_DEBUG, "Could not sendRaw command to SDK")
            //Log.e(Tags.TAG_IDENT_DEBUG, e.message)
        }
    }

    private fun handleMessage(messageJson: String) {
        // Workaround for extracting the url from the messageJson.

        // TODO refactor for production
        if (messageJson.indexOf("url") != -1) {
            // it contains the response
            val s = messageJson.substring(messageJson.indexOf("url") + 6, messageJson.length-2);
            callback?.onCompleted(s)
        }

        Log.d(Tags.TAG_IDENT_DEBUG, messageJson);

        val message = IdentificationUtil.parseJson(messageJson)
        Log.d(Tags.TAG_IDENT_DEBUG, message.toString());

        if (message == null) {
            Log.d(Tags.TAG_IDENT_DEBUG, "Bad state")
            return
        }

        // Pass raw sdk messageJson to callback for debugging/migration
        //callback.onMessage(message)

        when(message.msg) {
            IdentificationUtil.MSG_AUTH -> {
                if(!message.result?.description.isNullOrEmpty() || message.result?.major == "http://www.bsi.bund.de/ecard/api/1.1/resultmajor#error"){
                    // An error occured
                    callback?.onError(message.result!!.description)
                    authInProgress = false
                    return
                }
                else if(messageJson.indexOf("url") != -1){
                    val s = messageJson.substring(messageJson.indexOf("url") + 6, messageJson.length-2);
                    //callback.onComplete(s)
                }
                else{
                    authInProgress = true
                }
            };
            IdentificationUtil.MSG_ACCESS_RIGHTS -> {
                callback?.onRequestAccessRights(message.chat!!.effective!!)

                // TODO dont automatically accept the access rights. This should be based on user interaction.
                //sendCommand(IdentificationUtil.CMD_ACCEPT)
            }

            // TODO remove state and mode, mostly deprecated in favor of callback
            IdentificationUtil.MSG_INSERT_CARD -> {
                callback?.onCardRecognized(message.card)
            }
            IdentificationUtil.MSG_ENTER_PIN -> {
                this.mode = IdentMode.PIN
                callback?.onRequestPin()
            }
            IdentificationUtil.MSG_ENTER_PUK -> {
                this.mode = IdentMode.PUK
                callback?.onRequestPuk()
            }
            IdentificationUtil.MSG_ENTER_CAN -> {
                this.mode = IdentMode.CAN
                callback?.onRequestCan()
            }
//            IdentificationUtil.MSG_READER -> {
//                callback?.onCardRecognized(message.card)
//            }
//            else -> Log.d(TAG, "Unhandled messageJson ${message}")
        }

    }

    interface Callback {
        fun onCompleted(resultUrl: String)
        fun onRequestAccessRights(accessRights: List<String>)
        fun onCardRecognized(card: Card?) // when the card is null, there is no card available
        fun onRequestPin()
        fun onRequestPuk()
        fun onRequestCan()
        fun onError(error: String)
    }

    private var authInProgress: Boolean = false
    private var mode: IdentMode = IdentMode.PIN

    companion object {
        const val STATE_DEFAULT = "Default"
        const val STATE_CONNECTED = "Connected"
        const val STATE_DISCONNECTED = "Disconnected"
        const val STATE_INSERT = "Insert"
        const val STATE_ACCESSRIGHTS = "AccessRights"
        const val STATE_ENTER_PIN = "Pin"
        const val STATE_ENTER_CAN = "Can"
        const val STATE_ENTER_PUK = "Puk"
        const val STATE_COMPLETE = "Completed"
        const val STATE_BAD = "Bad"
        const val STATE_CARD_INSERTED = "Inserted"
    }

    /*
    AUSWEISAPP2 SDK Communication

    TODO: Error Callbacks
    TODO: State modeling

     */

    var sdk: IAusweisApp2Sdk? = null
    private var sdkConnection: ServiceConnection? = null

    private var sdkCallback = object : IAusweisApp2SdkCallback.Stub() {
        var mSessionID: String? = null

        @Throws(RemoteException::class)
        override fun sessionIdGenerated(
                pSessionId: String, pIsSecureSessionId: Boolean) {
            mSessionID = pSessionId
        }

        @Throws(RemoteException::class)
        override fun receive(pJson: String) {
            handleMessage(pJson)
        }

        @Throws(RemoteException::class)
        override fun sdkDisconnected() {
            Log.d(Tags.TAG_IDENT_DEBUG, "SDK Disconnected")
        }
    }

    fun bindIdIdentificationService(context: Context) {
        Log.d(Tags.TAG_IDENT_DEBUG, "Binding auth service... ")

        sdkConnection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                Log.d(Tags.TAG_IDENT_DEBUG, "Service Connected")

                try {
                    sdk = IAusweisApp2Sdk.Stub.asInterface(service)
                    connectSDK()
                } catch (e: ClassCastException) {
                    Log.e(Tags.TAG_IDENT_DEBUG, e.message)
                }
                // TODO callback
            }

            override fun onServiceDisconnected(className: ComponentName) {
                // TODO callback
                Log.d(Tags.TAG_IDENT_DEBUG, "Service Disconnected")
                sdk = null
            }
        }
        val name = "com.governikus.ausweisapp2.START_SERVICE"
        val serviceIntent = Intent(name)
        serviceIntent.setPackage(context.packageName)
        context.bindService(serviceIntent, sdkConnection, Context.BIND_AUTO_CREATE)
    }

    private fun connectSDK() {
        Log.d(Tags.TAG_IDENT_DEBUG, "Binding SDK...")
        try {
            if (!sdk!!.connectSdk(sdkCallback)) {

                // TODO error
                // already connected? Handle error...
                Log.d(Tags.TAG_IDENT_DEBUG, "Connection Issue")
            }
        } catch (e: RemoteException) {
            // handle exception
            Log.e(Tags.TAG_IDENT_DEBUG, e.toString())
            // TODO error
        }
    }

    internal fun dispatchNfcTag(tag: Tag){
        try {
            sdk?.updateNfcTag(sdkCallback.mSessionID, tag)
        } catch (e: Exception) {
            // TODO error
            Log.d(Tags.TAG_IDENT_DEBUG, "An error occured updating/dispating a NFC Tag")
        }
    }
}
