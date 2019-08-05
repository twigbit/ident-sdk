package com.twigbit.identsdk.ausweisident

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("given_name")
    val givenName: String?,
    @SerializedName("family_name")
    val familyName: String?,
    @SerializedName("name")
    val name: String?,
    /**
     * birthdate
     *
     * Format: YYYY-MM-DD
     */
    @SerializedName("birthdate")
    val birthdate: String?,
    @SerializedName("https://ref-ausweisident.eid-service.de/addressType")
    val addressType: String?,
    @SerializedName("address")
    val address: UserInfo.Address?,
    @SerializedName("artistic_name")
    val artisticName: String?,

    @SerializedName("https://ref-ausweisident.eid-service.de/birthname")
    val birthname: String?,
    @SerializedName("https://ref-ausweisident.eid-service.de/nationality")
    val nationality: String?,
    @SerializedName("https://ref-ausweisident.eid-service.de/academicTitle")
    val academicTitle: String,
    @SerializedName("https://ref-ausweisident.eid-service.de/issuingState")
    val issuingState: String,
    @SerializedName("https://ref-ausweisident.eid-service.de/restrictedId")
    val restrictedId: String,
    @SerializedName("https://ref-ausweisident.eid-service.de/placeOfBirthType")
    val placeOfBirthType: String?,
    @SerializedName("https://ref-ausweisident.eid-service.de/placeOfBirth")
    val placeOfBirth: UserInfo.Address?,
    @SerializedName("https://ref-ausweisident.eid-service.de/documentType")
    val documentType: String?,
    @SerializedName("https://ref-ausweisident.eid-service.de/residencePermitI")
    val residencePermitI: String?,

    val iss: String?,
    val sub: String?,
    val aud: String?,
    val iat: String?,
    val jti: String?


) {
    data class Address(
        /**
         * One letter country code
         * @example "D"
         */
        val country: String?,
        val streetAddress: String?,
        val formatted: String?,
        val postalCode: String?,
        val locality: String?,
        val region: String?
    )

    override fun toString(): String {
        return "UserInfo(givenName=$givenName, familyName=$familyName, name=$name, birthdate=$birthdate, " +
                "addressType=$addressType, address=$address, artisticName=$artisticName, birthname=$birthname, nationality=$nationality, academicTitle='$academicTitle', issuingState='$issuingState', restrictedId='$restrictedId', placeOfBirthType=$placeOfBirthType, placeOfBirth=$placeOfBirth, documentType=$documentType, residencePermitI=$residencePermitI, iss=$iss, sub=$sub, aud=$aud, iat=$iat, jti=$jti" +
                ")"
    }
}
