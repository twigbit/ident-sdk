package com.twigbit.identsdk.util

import android.content.Context
import com.twigbit.identsdk.R
import com.twigbit.identsdk.ausweisident.AusweisIdentScopes

object StringUtil{
    fun translate(context: Context, string: String): String{
        val id = when (string){
            AusweisIdentScopes.ACADEMIC_TITLE -> R.string.access_right_academit_title
            AusweisIdentScopes.ARTISTIC_NAME -> R.string.access_right_academit_title
            AusweisIdentScopes.BIRTH_NAME -> R.string.access_right_academit_title
            AusweisIdentScopes.DATE_OF_BIRTH -> R.string.access_right_academit_title
            AusweisIdentScopes.DATE_OF_EXPIRY -> R.string.access_right_academit_title
            AusweisIdentScopes.DOCUMENT_TYPE -> R.string.access_right_academit_title
            AusweisIdentScopes.FAMILY_NAME -> R.string.access_right_academit_title
            AusweisIdentScopes.GIVEN_NAMES -> R.string.access_right_academit_title
            AusweisIdentScopes.ISSUING_STATE -> R.string.access_right_academit_title
            AusweisIdentScopes.NATIONALITY -> R.string.access_right_academit_title
            AusweisIdentScopes.RESTRICTED_ID -> R.string.access_right_academit_title
            AusweisIdentScopes.PLACE_OF_BIRTH -> R.string.access_right_academit_title
            AusweisIdentScopes.PLACE_OF_RECIDENCE -> R.string.access_right_academit_title
            else -> R.string.unknown_value
        }
        return context.getString(id)
    }
}