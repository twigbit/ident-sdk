package com.twigbit.identsdk.ausweisident

import android.net.Uri

/**
 * Usage:
 *
 * ```
 * val ausweisIdentTcTokenUrl: String = AusweisIdentBuilder()
 *   .scope(AUSWEISIDENT_SCOPE_FAMILYNAMES)
 *   .scope(AUSWEISIDENT_SCOPE_PLACEOFBIRTH)
 *   .state("123456")
 *   .clientId("ABCDEFG")
 *   .redirectUrl("https://yourserver.com")
 *   .build()
 */
class AusweisIdentBuilder {

    private var host: String = OPENID_HOST
    private var state: String = ""
    private var nonce: String = ""
    private var clientId: String = ""
    private var redirectUrl: String = ""
    private val scopes: MutableSet<String> = LinkedHashSet()

    fun ref(enable: Boolean = true) = apply { host = if (enable) OPENID_HOST_REF else OPENID_HOST }
    fun scope(scope: String) = apply { scopes.add(scope) }
    fun state(state: String) = apply { this.state = state }
    fun nonce(nonce: String) = apply { this.nonce = nonce }
    fun clientId(clientId: String) = apply { this.clientId = clientId }
    fun redirectUrl(redirectUrl: String) = apply { this.redirectUrl = redirectUrl }

    fun build(): String {
        val b = Uri.Builder()

        b.scheme("https://")
        b.authority(host)
        b.appendPath(OPENID_AUTHORIZATION_ENDPOINT)

        b.appendQueryParameter("clientId", clientId)
        b.appendQueryParameter("redirect_uri", redirectUrl)

        if (scopes.isNotEmpty()) b.appendQueryParameter("scopes", scopes.joinToString(" "))
        if (state.isNotBlank()) b.appendQueryParameter("state", state)
        if (nonce.isNotBlank()) b.appendQueryParameter("nonce", nonce)

        b.appendQueryParameter("response_type", "code")
        b.appendQueryParameter("acr_values", "integrated")

        return b.build().toString()
    }

    companion object {
        const val OPENID_HOST = "ausweis-ident.de"
        const val OPENID_HOST_REF = "ref-ausweisident.eid-service.de"
        const val OPENID_AUTHORIZATION_ENDPOINT = "/oic/authorize"
    }
}
