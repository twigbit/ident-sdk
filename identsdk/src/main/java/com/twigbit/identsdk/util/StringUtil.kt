package com.twigbit.identsdk.util

import android.content.Context
import com.twigbit.identsdk.R
import com.twigbit.identsdk.ausweisident.AusweisIdentScopes
import com.twigbit.identsdk.model.AccessRights

object StringUtil{
    fun translate(context: Context, string: String): String{
        val id = when (string){
            AccessRights.ACADEMIC_TITLE -> R.string.access_right_academic_title
            AccessRights.ARTISTIC_NAME -> R.string.access_right_artistic_name
            AccessRights.BIRTH_NAME -> R.string.access_right_birth_name
            AccessRights.DATE_OF_BIRTH -> R.string.access_right_date_of_birth
            AccessRights.DATE_OF_EXPIRY -> R.string.access_right_date_of_expiry
            AccessRights.DOCUMENT_TYPE -> R.string.access_right_doc_type
            AccessRights.FAMILY_NAME -> R.string.access_right_family_name
            AccessRights.GIVEN_NAMES -> R.string.access_right_given_name
            AccessRights.ISSUING_STATE -> R.string.access_right_issuing_state
            AccessRights.NATIONALITY -> R.string.access_right_nationality
            AccessRights.RESTRICTED_ID -> R.string.access_right_restricted_id
            AccessRights.PLACE_OF_BIRTH -> R.string.access_right_place_of_pirth
            AccessRights.PLACE_OF_RECIDENCE -> R.string.access_right_place_of_residence
            else -> R.string.unknown_value
        }
        return context.getString(id)
    }
}