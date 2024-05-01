package de.hausdinga.stuxpass.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TicketLogo(
    val src: String,
    val alt: String
)

@JsonClass(generateAdapter = true)
data class NoticeTexts(
    val deutschlandticket: String,
    val deutschlandsemesterticket: String
)

@JsonClass(generateAdapter = true)
data class PartnerJson(
    val ticketLogos: List<TicketLogo>,
    val ticketNoticeTextForTicketNames: NoticeTexts
)