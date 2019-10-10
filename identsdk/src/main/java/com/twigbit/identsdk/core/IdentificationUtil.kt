/*
 * Copyright (c) 2018. Moritz Morgenroth- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Moritz Morgenroth <development@moritzmorgenroth.de>
 */

package com.twigbit.identsdk.core

import com.google.gson.Gson
import java.net.URLEncoder

object IdentificationUtil {
    const val MSG_ACCESS_RIGHTS = "ACCESS_RIGHTS"
    const val MSG_ENTER_PIN = "ENTER_PIN"
    const val MSG_ENTER_PUK = "ENTER_PUK"
    const val MSG_ENTER_CAN = "ENTER_CAN"
    const val MSG_INSERT_CARD = "INSERT_CARD"
    const val MSG_INSERT_CERTIFICATE = "CERTIFICATE"
    const val MSG_BAD_STATE = "BAD_STATE"
    const val MSG_READER = "READER"
    const val MSG_CUSTOM_URL = "URL"
    const val MSG_AUTH = "AUTH"
    const val CMD_RUN_AUTH = "RUN_AUTH"
    const val CMD_GET_CERTIFICATE = "GET_CERTIFICATE"
    const val CMD_ACCEPT = "ACCEPT"
    const val CMD_CANCEL = "CANCEL"
    const val CMD_GET_READER = "GET_READER_LIST"
    const val CMD_SET_PIN = "SET_PIN"
    const val CMD_SET_PUK = "SET_PUK"
    const val CMD_SET_CAN = "SET_CAN"
    const val PARAM_TCTOKEN = "tcTokenURL"
    const val PARAM_VALUE = "value"
    const val PARAM_CMD = "cmd"


    val gson = Gson()
    fun parseJson(msg: String): Message? {
        return gson.fromJson(msg, Message::class.java)
    }

    fun buildTokenUrl(redirectUrl: String, clientId: String): String {
        return "https://ref-ausweisident.eid-service.de/oic/authorize?scope=FamilyNames+GivenNames+AcademicTitle+PlaceOfBirth+DateOfBirth+PlaceOfResidence+&response_type=code&redirect_uri=${URLEncoder.encode(redirectUrl, "UTF-8")}&state=123456&&client_id=$clientId&acr_values=integrated"
    }
    fun buildCmdString(cmd: String, payload: Pair<String, String>? = null): String{
        return "{\"$PARAM_CMD\": \"${cmd}\" " + (if (payload!= null) ", \"${payload.first}\": \"${payload.second}\"" else "") + "}"
    }
}

class Command {
    val cmd: String = ""
}

// "aux":{"validityDate":"2019-05-09"},"chat":{"effective":["Address","PlaceOfBirth","DateOfBirth","DoctoralDegree","FamilyName","GivenNames"],"optional":[],"required":["Address","PlaceOfBirth","DateOfBirth","DoctoralDegree","FamilyName","GivenNames"]}
// {"description":{"issuerName":"D-Trust GmbH","issuerUrl":"http://www.d-trust.net","purpose":"AusweisIDent - Online Ausweis Identifizierungsservice der Bundesdruckerei GmbH","subjectName":"Bundesdruckerei GmbH","subjectUrl":"https://ref-ausweisident.eid-service.de","termsOfUsage":"Name, Anschrift und E-Mail-Adresse des Diensteanbieters:\r\nBundesdruckerei GmbH\r\nOlaf Clemens\r\nKommandantenstraße 18\r\n10969 Berlin\r\nsupport@bdr.de\r\n\r\nGeschäftszweck:\r\nAusweisIDent - Online Ausweis Identifizierungsservice der Bundesdruckerei GmbH\r\n\r\nHinweis auf die für den Diensteanbieter zuständigen Stellen, die die Einhaltung der Vorschriften zum Datenschutz kontrollieren:\r\nDie Bundesbeauftragte für den Datenschutz und die Informationsfreiheit\r\nHusarenstraße 30\r\n53117 Bonn\r\n+49 (0)228 997799-0\r\npoststelle@bfdi.bund.de\r\n"},"msg":"CERTIFICATE","validity":{"effectiveDate":"2019-06-24","expirationDate":"2019-06-25"}}

class Message {
    val msg: String = ""
    val name: String = ""
    val card: Card? = null
    val result: Result? = null
    val chat: AccessRightPayload? = null
    val validity: CertificateValidity? = null
    val description: CertificateInfo? = null
    // TODO add description param for certificates
    override fun toString(): String {
        return "Message(msg='$msg', name='$name', card=$card, result=$result, chat=$chat)"
    }

}

class CertificateInfo{
    val issuerName: String = ""
    val issuerUrl: String = ""
    val purpose: String = ""
    val subjectName: String = ""
    val subjectUrl: String = ""
    val termsOfUsage: String = ""
    override fun toString(): String {
        return "CertificateInfo(issuerName=$issuerName, purpose=$purpose, subjectName=$subjectName)"
    }
}
class CertificateValidity{
    val effectiveDate: String = ""
    val expirationDate: String = ""
    override fun toString(): String {
        return "CertificateValidity(effectiveDate=$effectiveDate, expirationDate=$expirationDate)"
    }
}

class AccessRightPayload{
    val effective: List<String>? = null;
    val optional: List<String>? = null;
    val required: List<String>? = null;
    override fun toString(): String {
        return "AccessRightPayload(effective=$effective, optional=$optional, required=$required)"
    }

}

class Card {
    val deactivated: Boolean = true;
    val inoperative: Boolean = false;
    val retryCounter: Int = -1;
    override fun toString(): String {
        return "Card(deactivated=$deactivated, inoperative=$inoperative, retryCounter=$retryCounter)"
    }

}

class Result {
    val major: String = ""
    val url: String = ""
    val description: String = ""

    override fun toString(): String {
        return "Result(major='$major', url='$url', description='$description' )"
    }
}

enum class IdentMode {
    PIN,
    PUK,
    CAN
}
