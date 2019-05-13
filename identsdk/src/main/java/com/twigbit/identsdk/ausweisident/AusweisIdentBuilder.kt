package com.twigbit.identsdk.ausweisident

import android.net.Uri

/**
 * Enables declarative building of the `tcTokenUrl` required for AusweisIdent.
 *
 * Fluid API to construct a `tcTokenUrl`.
 * Calls to [clientId] and [redirectUrl] methods are required.
 *
 * Example:
 *
 * ```
 * val ausweisIdentTcTokenUrl: String = AusweisIdentBuilder()
 *   .scope(AusweisIdentScopes.BirthName)
 *   .scope(AusweisIdentScopes.GivenNames)
 *   .state("123456")
 *   .clientId("ABCDEFG")
 *   .redirectUrl("https://yourserver.com")
 *   .build()
 * ```
 *
 */
class AusweisIdentBuilder {

    private var host: String = OPENID_HOST
    private var state: String = ""
    private var nonce: String = ""
    private var clientId: String = ""
    private var redirectUrl: String = ""
    private val scopes: MutableSet<String> = LinkedHashSet()

    /**
     * Set the AusweisIdent Server Endpoint.
     *
     * @param host Must not contain the scheme (e.g. ausweis-ident.de and not https://ausw...)
     */
    fun host(host: String) = apply { this.host = host }

    /**
     * Set the AusweisIdent Server Endpoint to the default one for the reference / test system.
     *
     * @param enable Weather to enable or disable the reference endpoint.
     */
    fun ref(enable: Boolean = true) = apply { host = if (enable) OPENID_HOST_REF else OPENID_HOST }

    /**
     * Add a scope. See [AusweisIdentScopes] for currently available ones.
     *
     * @param scope String Identifier of the scope. Check AusweisIdent docs for details.
     */
    fun scope(scope: String) = apply { scopes.add(scope) }

    /**
     * Set the OpenID state parameter
     */
    fun state(state: String) = apply { this.state = state }

    /**
     * Set the OpenID nonce parameter
     */
    fun nonce(nonce: String) = apply { this.nonce = nonce }

    /**
     * Set your the AusweisIdent clientId.
     */
    fun clientId(clientId: String) = apply { this.clientId = clientId }

    /**
     * Set your AusweisIdent redirectUrl. Must match the one, you set in your AusweisIdent registration.
     */
    fun redirectUrl(redirectUrl: String) = apply { this.redirectUrl = redirectUrl }

    /**
     * Build the `tcTokenUrl`
     */
    fun build(): String {
        val b = Uri.Builder()

        b.scheme("https")
        b.authority(host)
        b.appendEncodedPath(OPENID_AUTHORIZATION_ENDPOINT)

        b.appendQueryParameter("client_id", clientId)
        b.appendQueryParameter("redirect_uri", redirectUrl)

        if (scopes.isNotEmpty()) b.appendQueryParameter("scope", scopes.joinToString(" "))
        if (state.isNotBlank()) b.appendQueryParameter("state", state)
        if (nonce.isNotBlank()) b.appendQueryParameter("nonce", nonce)

        b.appendQueryParameter("response_type", "code")
        b.appendQueryParameter("acr_values", "integrated")

        return b.build().toString()
    }

    companion object {
        const val OPENID_HOST = "ausweis-ident.de"
        const val OPENID_HOST_REF = "ref-ausweisident.eid-service.de"
        const val OPENID_AUTHORIZATION_ENDPOINT = "oic/authorize"
    }
}
