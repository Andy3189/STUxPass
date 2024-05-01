package de.hausdinga.stuxpass.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BearerAuth(private val token: String) {

    fun getAuth(): String {
        return "Bearer $token"
    }
}