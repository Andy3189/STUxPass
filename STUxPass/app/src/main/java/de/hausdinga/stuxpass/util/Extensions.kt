package de.hausdinga.stuxpass.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatTime(): String {
    return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(this)
}

fun LocalDate.formatDate(): String {
    return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(this)
}

fun LocalDate.rideFormat(): String {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(this)
}

fun Long.toLocalDate(): LocalDate? {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}