package com.twigbit.identsdk.util

import android.content.Context
import com.twigbit.identsdk.R
import com.twigbit.identsdk.ausweisident.AusweisIdentScopes

object StringUtil{
    fun translate(context: Context, string: String): String{
        val id = when (string){
            AusweisIdentScopes.ACADEMIC_TITLE -> R.string.access_right_academic_title
            AusweisIdentScopes.ARTISTIC_NAME -> R.string.access_right_artistic_name
            AusweisIdentScopes.BIRTH_NAME -> R.string.access_right_birth_name
            AusweisIdentScopes.DATE_OF_BIRTH -> R.string.access_right_date_of_birth
            AusweisIdentScopes.DATE_OF_EXPIRY -> R.string.access_right_date_of_expiry
            AusweisIdentScopes.DOCUMENT_TYPE -> R.string.access_right_doc_type
            AusweisIdentScopes.FAMILY_NAME -> R.string.access_right_family_name
            AusweisIdentScopes.GIVEN_NAMES -> R.string.access_right_given_name
            AusweisIdentScopes.ISSUING_STATE -> R.string.access_right_issuing_state
            AusweisIdentScopes.NATIONALITY -> R.string.access_right_nationality
            AusweisIdentScopes.RESTRICTED_ID -> R.string.access_right_restricted_id
            AusweisIdentScopes.PLACE_OF_BIRTH -> R.string.access_right_place_of_pirth
            AusweisIdentScopes.PLACE_OF_RECIDENCE -> R.string.access_right_place_of_residence
            else -> R.string.unknown_value
        }
        return context.getString(id)
    }
}