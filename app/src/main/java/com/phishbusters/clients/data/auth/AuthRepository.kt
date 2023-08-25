package com.phishbusters.clients.data.auth

interface AuthRepository {
    fun login(username: String, password: String)
    fun register(username: String, password: String)
    fun logout()
}