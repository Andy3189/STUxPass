package de.hausdinga.stuxpass.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserToken(
    val access_token: String,
    val expires_in: Int,
    val refresh_expires_in: Int,
    val refresh_token: String,
    val token_type: String,
    val session_state: String
)