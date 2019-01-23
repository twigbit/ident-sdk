/*
 * Copyright (c) 2018. Moritz Morgenroth- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Moritz Morgenroth <development@moritzmorgenroth.de>
 */

package com.twigbit.identsdk

import com.google.gson.Gson
import java.net.URLEncoder

object IdentificationUtil {
    val MSG_ACCESS_RIGHTS = "ACCESS_RIGHTS"
    val MSG_ENTER_PIN = "ENTER_PIN"
    val MSG_ENTER_PUK = "ENTER_PUK"
    val MSG_ENTER_CAN = "ENTER_CAN"
    val MSG_INSERT_CARD = "INSERT_CARD"
    val MSG_BAD_STATE = "BAD_STATE"
    val MSG_READER = "READER"
    val MSG_CUSTOM_URL = "URL"
    val MSG_AUTH = "AUTH"
    val CMD_GET_CERTIFICATE = "GET_CERTIFICATE"
    val CMD_ACCEPT = "ACCEPT"
    val CMD_GET_READER = "GET_READER_LIST"
    val CMD_SET_PIN = "SET_PIN"
    val CMD_SET_PUK = "SET_PUK"
    val CMD_SET_CAN = "SET_CAN"

    val gson = Gson()
    fun parseJson(msg: String): Message? {
        return gson.fromJson(msg, Message::class.java)
    }

    fun buildTokenUrl(redirectUrl: String, clientId: String): String {
        return "https://ref-ausweisident.eid-service.de/oic/authorize?scope=FamilyNames+GivenNames+AcademicTitle+PlaceOfBirth+DateOfBirth+PlaceOfResidence+&response_type=code&redirect_uri=${URLEncoder.encode(redirectUrl, "UTF-8")}&state=123456&&client_id=$clientId&acr_values=integrated"
    }
}

class Command {
    val cmd: String = ""
}

class Message {
    val msg: String = ""
    val name: String = ""
    val card: Card? = null
    val result: Result? = null

    override fun toString(): String {
        return "Message(msg='$msg', name='$name', result='$result',  card='$card')"
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
