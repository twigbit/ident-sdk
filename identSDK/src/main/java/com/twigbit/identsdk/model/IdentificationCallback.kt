package com.twigbit.identsdk.model

import com.twigbit.identsdk.model.IdentificationError

interface IdentificationCallback {
    fun onCompleted(resultUrl: String)
    fun onRequestAccessRights(accessRights: ArrayList<String>)
    fun onCardRecognized(card: IdentificationCard)
    fun onRequestPin()
    fun onRequestPuk()
    fun onRequestCan()
    fun onError(error: IdentificationError)
}