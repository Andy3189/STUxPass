package de.hausdinga.stuxpass.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookingID (val bookingId: String)