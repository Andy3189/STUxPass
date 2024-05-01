package de.hausdinga.stuxpass.repository.remote

import de.hausdinga.stuxpass.local.database.Ticket

interface TicketWebService {
    suspend fun loadTickets(birthDate: String, immaNr: String): List<Ticket>
}