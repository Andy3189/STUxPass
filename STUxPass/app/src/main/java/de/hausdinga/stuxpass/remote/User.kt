package de.hausdinga.stuxpass.remote

import com.squareup.moshi.JsonClass
import de.hausdinga.stuxpass.util.Constants

@JsonClass(generateAdapter = true)
data class User(
    val birthday: String,
    val matriculationNumber: String,
    val partnerId: String = Constants.TU_RIDE_PARTNER_ID,
    val authRealmName: String = Constants.RIDE_REALM_ID
)