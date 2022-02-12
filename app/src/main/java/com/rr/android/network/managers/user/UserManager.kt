package com.rr.android.network.managers.user

import com.rr.android.network.models.User
import com.rr.android.network.models.UserSerializer
import com.rr.android.util.extensions.Data

interface UserManager {
    suspend fun signUp(user: User): Result<Data<UserSerializer>>
    suspend fun signIn(user: User): Result<Data<UserSerializer>>
    suspend fun signOut(): Result<Data<Void>>
}
