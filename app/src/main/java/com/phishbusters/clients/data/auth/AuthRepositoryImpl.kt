package com.phishbusters.clients.data.auth

import com.phishbusters.clients.data.TokenStore
import com.phishbusters.clients.network.ApiService

class AuthRepositoryImpl(private val apiService: ApiService, private val tokenStore: TokenStore) :
    AuthRepository {

    override fun login(username: String, password: String) {
        tokenStore.saveToken("token")
    }

    override fun register(username: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}