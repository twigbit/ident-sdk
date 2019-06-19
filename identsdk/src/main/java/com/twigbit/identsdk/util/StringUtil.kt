package com.twigbit.identsdk.util

import android.content.Context
import com.twigbit.identsdk.R
import com.twigbit.identsdk.core.AccessRights

object StringUtil{
    fun translate(context: Context, string: String): String{
        val id = when (string){
            AccessRights.ADDRESS -> R.string.twigbit_ident_access_right_address
            AccessRights.BIRTH_NAME -> R.string.twigbit_ident_access_right_birth_name
            AccessRights.FAMILY_NAME -> R.string.twigbit_ident_access_right_family_name
            AccessRights.GIVEN_NAMES -> R.string.twigbit_ident_access_right_given_names
            AccessRights.PLACE_OF_BIRTH -> R.string.twigbit_ident_access_right_place_of_birth
            AccessRights.DATE_OF_BIRTH -> R.string.twigbit_ident_access_right_date_of_birth
            AccessRights.DOCTORAL_DEGREE -> R.string.twigbit_ident_access_right_doctoral_degree
            AccessRights.ARTISTIC_NAME -> R.string.twigbit_ident_access_right_artistic_name
            AccessRights.PSEUDONYM -> R.string.twigbit_ident_access_right_pseudonym
            AccessRights.VALID_UNTIL -> R.string.twigbit_ident_access_right_valid_until
            AccessRights.NATIONALITY -> R.string.twigbit_ident_access_right_nationality
            AccessRights.ISSUING_COUNTRY -> R.string.twigbit_ident_access_right_issuing_country
            AccessRights.DOCUMENT_TYPE -> R.string.twigbit_ident_access_right_document_type
            AccessRights.RESIDENCE_PERMIT_I -> R.string.twigbit_ident_access_right_residence_permit_i
            AccessRights.RESIDENCE_PERMIT_II -> R.string.twigbit_ident_access_right_residence_permit_ii
            AccessRights.COMMUNITY_ID -> R.string.twigbit_ident_access_right_community_id
            AccessRights.ADDRESS_VERIFICATION -> R.string.twigbit_ident_access_right_address_verification
            AccessRights.AGE_VERIFICATION -> R.string.twigbit_ident_access_right_age_verification
            else -> R.string.twigbit_ident_access_right_unknown_value
        }
        return context.getString(id)
    }
}