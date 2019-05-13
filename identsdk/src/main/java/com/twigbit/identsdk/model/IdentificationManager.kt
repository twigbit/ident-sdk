/*
 * Copyright (c) 2018. Moritz Morgenroth- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Moritz Morgenroth <development@moritzmorgenroth.de>
 */

package com.twigbit.identsdk.model


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
import com.twigbit.identsdk.util.IdentMode
import com.twigbit.identsdk.util.IdentificationUtil
import com.twigbit.identsdk.util.StringUtil

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

    @Deprecated("Deprecated in favor of AusweisIdentHelper configuration helper")
    fun startIdentWithAusweisIdent(redirectUri: String, clientId: String) {
        val cmd = "{\"cmd\": \"${IdentificationUtil.CMD_RUN_AUTH}\", \"${IdentificationUtil.PARAM_TCTOKEN}\": \"${IdentificationUtil.buildTokenUrl(redirectUri, clientId)}\" }"
        send(cmd)
    }

    fun startIdent(tokenURL: String) {
        //        val cmd = "{\"cmd\": \"${IdentificationUtil.CMD_RUN_AUTH}\", \"${IdentificationUtil.PARAM_TCTOKEN}\": \"${tokenURL}\" }"
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_RUN_AUTH, Pair(IdentificationUtil.PARAM_TCTOKEN, tokenURL)))
    }

    fun setPin(pin: String){
//        val cmd = "{\"cmd\": \"${IdentificationUtil.CMD_SET_PIN}\", \"${IdentificationUtil.PARAM_VALUE}\": \"${pin}\"}"
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_RUN_AUTH, Pair(IdentificationUtil.PARAM_VALUE, pin)))
    }
    fun setPuk(puk: String){
//        val cmd = "{\"cmd\": \"${IdentificationUtil.CMD_SET_PUK}\", \"${IdentificationUtil.PARAM_VALUE}\": \"${puk}\"}"
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_SET_PUK, Pair(IdentificationUtil.PARAM_VALUE, puk)))

    }
    fun setCan(can: String){
//        val cmd = "{\"cmd\": \"${IdentificationUtil.CMD_SET_CAN}\", \"${IdentificationUtil.PARAM_VALUE}\": \"${can}\"}"
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_SET_CAN, Pair(IdentificationUtil.PARAM_VALUE, can)))
    }
    fun acceptAccessRights(){
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_ACCEPT))
    }
    fun cancel(){
//        val cmd = "{\"cmd\": \"${IdentificationUtil.CMD_CANCEL}\" }"
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_CANCEL))
    }
    fun getCertificate(){
        send(IdentificationUtil.buildCmdString(IdentificationUtil.CMD_GET_CERTIFICATE))
    }

    // TODO implement getCertificate

    // temporary method to make IdentificationLogic reusable in Showcase
    @Deprecated("This method is only available for early adopter migration and debugging, will be removed for production")
    fun sendRaw(cmd: String){
        send(cmd)
    }


    private fun sendCommand(cmd: String?) {
        if(cmd == null) return;
        val cmdJson = "{\"cmd\": \"${cmd}\"}"
        send(cmdJson)
    }

    private fun send(cmd: String) {
        Log.d(TAG,  "Sending command: " + cmd)
        try {
            if (!sdk!!.send(sdkCallback.mSessionID, cmd)) {
                // TODO error
                Log.e(TAG, "Could not sendRaw command to SDK")
            }
        } catch (e: Exception) {
            // TODO error
            // FIXME 20190509 This sometimes gets called without an error message. Possibly due to improperly terminated background service. 
            Log.e(TAG, "Could not sendRaw command to SDK")
            //Log.e(TAG, e.message)
        }
    }

    private fun handleMessage(messageJson: String) {
        // Workaround for extracting the url from the messageJson.

        // TODO refactor for production
//        if (messageJson.indexOf("url") != -1) {
//            // it contains the response
//            val s = messageJson.substring(messageJson.indexOf("url") + 6, messageJson.length-2);
//            state = STATE_COMPLETE
//            callback.onComplete(s)
//        }

        Log.d(TAG, messageJson);

        val message = IdentificationUtil.parseJson(messageJson)
        Log.d(TAG, message.toString());

        if (message == null) {
            state = STATE_BAD
            Log.d(TAG, "Bad state")
            return
        }

        // Pass raw sdk messageJson to callback for debugging/migration
        //callback.onMessage(message)

        if(message.card != null){
            this.state = STATE_CARD_INSERTED
        }
        when(message.msg) {
            IdentificationUtil.MSG_AUTH -> {
                if(!message.result?.description.isNullOrEmpty() || message.result?.major == "http://www.bsi.bund.de/ecard/api/1.1/resultmajor#error"){
                    // An error occured
                    //callback.onError(message.result!!.description)
                    authInProgress = false
                    return
                }
                else if(messageJson.indexOf("url") != -1){
                    val s = messageJson.substring(messageJson.indexOf("url") + 6, messageJson.length-2);
                    state = STATE_COMPLETE
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
            IdentificationUtil.MSG_INSERT_CARD -> {
                this.state = STATE_INSERT
            }
            IdentificationUtil.MSG_ENTER_PIN -> {
                this.mode = IdentMode.PIN
                this.state = STATE_ENTER_PIN
            }
            IdentificationUtil.MSG_ENTER_PUK -> {
                this.mode = IdentMode.PUK

                this.state = STATE_ENTER_PUK
            }
            IdentificationUtil.MSG_ENTER_CAN -> {
                this.mode = IdentMode.CAN
                this.state = STATE_ENTER_CAN
            }


//            else -> Log.d(TAG, "Unhandled messageJson ${message}")
        }

    }

    interface Callback {
        fun onCompleted(resultUrl: String)
        fun onRequestAccessRights(accessRights: List<String>)
        fun onCardRecognized(card: IdentificationCard)
        fun onRequestPin()
        fun onRequestPuk()
        fun onRequestCan()
        fun onError(error: IdentificationError)
    }

    private var authInProgress: Boolean = false
    private var mode: IdentMode = IdentMode.PIN

    private var state: String = STATE_DEFAULT
        set(value) {
            field = value
        }

    companion object {
        const val TAG = "IdentificationManager"
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
            Log.d(TAG, "SDK Disconnected")
            state = STATE_DISCONNECTED
        }
    }

    fun bindIdIdentificationService(context: Context) {
        Log.d(TAG, "Binding auth service... ")

        sdkConnection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                Log.d(TAG, "Service Connected")

                try {
                    sdk = IAusweisApp2Sdk.Stub.asInterface(service)
                    connectSDK()
                } catch (e: ClassCastException) {
                    Log.e(TAG, e.message)
                }
                // TODO callback
            }

            override fun onServiceDisconnected(className: ComponentName) {
                // TODO callback
                Log.d(TAG, "Service Disconnected")
                sdk = null
                state = STATE_DISCONNECTED
            }
        }
        val name = "com.governikus.ausweisapp2.START_SERVICE"
        val serviceIntent = Intent(name)
        serviceIntent.setPackage(context.packageName)
        context.bindService(serviceIntent, sdkConnection, Context.BIND_AUTO_CREATE)
    }

    private fun connectSDK() {
        Log.d(TAG, "Binding SDK...")
        try {
            if (!sdk!!.connectSdk(sdkCallback)) {

                // TODO error
                // already connected? Handle error...
                Log.d(TAG, "Connection Issue")
            }
        } catch (e: RemoteException) {
            // handle exception
            Log.e(TAG, e.toString())
            // TODO error
        }
    }

    internal fun dispatchNfcTag(tag: Tag){
        try {
            sdk?.updateNfcTag(sdkCallback.mSessionID, tag)
        } catch (e: Exception) {
            // TODO error
            Log.d(TAG, "An error occured updating/dispating a NFC Tag")
        }
    }
}
