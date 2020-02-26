package com.twigbit.identsdk.ausweisident

import android.net.Uri
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.twigbit.identsdk.util.Tags
import okhttp3.*
import java.io.IOException
import java.net.CookieManager
import java.net.CookieStore
import java.net.URL
import java.net.URLEncoder

class AusweisIdentResultHandler(val callback: AusweisIdentResultHandler.Callback) {

    interface Callback {
        fun onError(message: String)
        fun onComplete(userInfo: UserInfo)
    }

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create()
    private var client: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(InMemoryCookieJar(CookieManager()))
        .build()

    fun fetchResult(resultUrl: String) {
        execRequest(HttpUrl.parse(resultUrl)!!)
    }

    private fun execRequest(url: HttpUrl) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Error while calling saml url")
                e.printStackTrace()
                callback.onError("Network error")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "Got response: $response")

                try {
                    if (!response.isSuccessful) throw IOException("Unexpected status code");

                    val body = response.body() ?: throw IOException("Unexpected empty body")

                    val userInfo = gson.fromJson(body.charStream(), UserInfo::class.java)

                    Log.d(Tags.TAG_IDENT_DEBUG, userInfo.toString())

                    callback.onComplete(userInfo)
                } catch (ex: IOException) {
                    Log.e(TAG, ex.toString())
                    ex.printStackTrace()
                    callback.onError("Unknown Error")
                }
            }
        })
    }

    companion object {
        const val TAG = "AusweisIdent"

        // AusweisIdent OpenID Connect Endpoints
        // const val ENDPOINT_OIC = "https://ausweis-ident.de/oic"
        const val ENDPOINT_OIC = "https://ref-ausweisident.eid-service.de/oic"
        const val ENDPOINT_OIC_AUTHORIZE = "$ENDPOINT_OIC/authorize"
        const val ENDPOINT_OIC_TOKEN = "$ENDPOINT_OIC/token"
        const val ENDPOINT_OIC_USERINFO = "$ENDPOINT_OIC/user-info"

        fun getTokenUrl(redirectUrl: String, clientId: String): String {
            val scopes = arrayOf(
                "BirthName",
                "FamilyNames",
                "GivenNames",
                "DateOfBirth",
                "PlaceOfResidence",
                "Nationality",
                "AcademicTitle",
                "ArtisticName",
                "IssuingState",
                "RestrictedID",
                "PlaceOfBirth",
                "DocumentType",
                "ResidencePermitI",
                "DateOfExpiry"
            ).joinToString("+")
            val encRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8")

            return "$ENDPOINT_OIC_AUTHORIZE?scope=$scopes&response_type=code&redirect_uri=$encRedirectUrl&state=123456&client_id=$clientId&acr_values=integrated"
        }
    }
}
