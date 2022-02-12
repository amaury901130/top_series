package com.rr.android.network.managers.session

import com.rr.android.network.models.User

interface SessionManager {
    var user: User?
    fun addAuthenticationHeaders(accessToken: String, client: String, uid: String)
    fun signOut()
    fun signIn(user: User)
    fun isUserSignedIn(): Boolean
}
