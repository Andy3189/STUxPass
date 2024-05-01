package de.hausdinga.stuxpass.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.hausdinga.stuxpass.local.database.Ticket
import de.hausdinga.stuxpass.repository.TicketApiRepository
import de.hausdinga.stuxpass.repository.TicketRepository
import de.hausdinga.stuxpass.repository.UserRepository
import de.hausdinga.stuxpass.util.rideFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val userRepository: UserRepository,
    private val apiRepository: TicketApiRepository
):  ViewModel() {

    val ticketsUiState: StateFlow<UiState> = ticketRepository
        .tickets.map {
            try {
                UiState.Success(it)
            } catch (ex: Exception) {
                UiState.Error(ex)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    private var _ticketUiState: MutableState<UiState> = mutableStateOf(UiState.Idle)
    val ticketUiState: State<UiState> = _ticketUiState

    private var _isLoadingTickets: MutableState<Boolean> = mutableStateOf(false)
    val isLoadingTickets: State<Boolean> = _isLoadingTickets

    fun deleteTicket(ticket: Ticket) {
        viewModelScope.launch {
            ticketRepository.delete(ticket)
        }

    }

    fun refreshTickets(onComplete: () -> Unit) {
        userRepository.getUsers().firstOrNull()?.let {//TODO handle more than 1 user
            loadTickets(it.first, it.second) { onComplete() }
        } ?: onComplete()
    }

    fun loadTickets(birthDate: LocalDate, immaNumber: String, onCompleted: (Throwable?) -> Unit) {
        loadTickets(birthDate.rideFormat(), immaNumber, onCompleted)
    }

    private fun loadTickets(birthDate: String, immaNumber: String, onCompleted: (Throwable?) -> Unit) {
        _isLoadingTickets.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tickets = apiRepository.loadTickets(birthDate, immaNumber)
                tickets.forEach { ticketRepository.add(it) }
                userRepository.addUser(
                    birthDate = birthDate,
                    immaNr = immaNumber
                )
                onCompleted(null)
                launch(Dispatchers.Main) {
                    _isLoadingTickets.value = false
                }
            } catch (ex: Exception) {
                onCompleted(ex)
                launch(Dispatchers.Main) {
                    _isLoadingTickets.value = false
                }
            }
        }
    }

    fun selectTicket(orderID: String) {
        _ticketUiState.value = UiState.Loading
        viewModelScope.launch {
            _ticketUiState.value = ticketRepository.load(orderID)?.let {
                UiState.Success(it)
            } ?: UiState.Error(Error("Couldnt load Ticket"))
        }
    }
}

sealed interface UiState {
    object Idle: UiState
    object Loading: UiState
    data class Error(val throwable: Throwable) : UiState
    data class Success<T>(val data: T) : UiState
}