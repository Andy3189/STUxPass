package de.hausdinga.stuxpass.repository

import de.hausdinga.stuxpass.local.database.Ticket
import de.hausdinga.stuxpass.repository.remote.RideApiWebService
import javax.inject.Inject
interface TicketApiRepository {
    suspend fun loadTickets(birthDate: String, immaNr: String): List<Ticket>
}
class RideTicketApiRepository @Inject constructor(private val ticketService: RideApiWebService): TicketApiRepository {

    override suspend fun loadTickets(birthDate: String, immaNr: String): List<Ticket> {
        return ticketService.loadTickets(birthDate, immaNr)
    }
}