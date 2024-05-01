package de.hausdinga.stuxpass.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.hausdinga.stuxpass.repository.DefaultTicketRepository
import de.hausdinga.stuxpass.repository.DefaultUserRepository
import de.hausdinga.stuxpass.repository.RideTicketApiRepository
import de.hausdinga.stuxpass.repository.TicketRepository
import de.hausdinga.stuxpass.repository.UserRepository
import de.hausdinga.stuxpass.repository.TicketApiRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsTicketsRepository(
        ticketRepository: DefaultTicketRepository
    ): TicketRepository


    @Singleton
    @Binds
    fun bindsUserRepository(
        userRepository: DefaultUserRepository
    ): UserRepository

    @Singleton
    @Binds
    fun bindsTicketApiRepository(
        ticketApiRepository: RideTicketApiRepository
    ): TicketApiRepository
}