package de.hausdinga.stuxpass.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.hausdinga.stuxpass.R
import de.hausdinga.stuxpass.local.database.Ticket
import de.hausdinga.stuxpass.ui.dialog.LoadingDialog
import de.hausdinga.stuxpass.ui.dialog.LoginDialog
import de.hausdinga.stuxpass.ui.theme.RAL1023
import de.hausdinga.stuxpass.ui.theme.STUxPassTheme
import de.hausdinga.stuxpass.util.Dimensions
import de.hausdinga.stuxpass.util.formatTime
import de.hausdinga.stuxpass.viewmodel.SharedViewModel
import de.hausdinga.stuxpass.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListView(
    viewModel: SharedViewModel = hiltViewModel(),
    navigateToTicket: (String) -> Unit
) {

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    val showLoadingDialog by viewModel.isLoadingTickets
    val ticketState by viewModel.ticketsUiState.collectAsStateWithLifecycle()
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.refreshTickets {
                refreshState.endRefresh()
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text ("Tickets") },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = { refreshState.startRefresh() }) {
                        Icon(Icons.Default.Refresh, "Trigger Refresh")
                    }

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                }
            ) {
                Image(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Ticket"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(refreshState.nestedScrollConnection),
        ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (ticketState is UiState.Success<*>) {
                LazyColumn(
                    content = {
                        if (!refreshState.isRefreshing) {
                            items((ticketState as UiState.Success<*>).data as List<Ticket>) {
                                TicketPreview(
                                    ticket = it,
                                    onClick = {
                                        navigateToTicket(it.orderID)
                                    }
                                )
                            }
                        }
                    }
                )
                PullToRefreshContainer(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = refreshState
                )
            }
        }
    }
    LoginDialog(
        showDialog = showAddDialog,
        onSubmit = { birthDate, immaNumber ->
            viewModel.loadTickets(birthDate, immaNumber) {
                showAddDialog = false
            }
        },
        onDismiss = { showAddDialog = false }
    )
    LoadingDialog(showDialog = showLoadingDialog)
}

@Composable
fun TicketPreview(
    ticket: Ticket,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .padding(Dimensions.LARGER),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimensions.MEDIUM,
            pressedElevation = Dimensions.SMALL
        )

    ) {
        Surface(
            color = RAL1023,
            onClick = onClick
        ) {
            Column {
                PreviewTicketTitleView(title = ticket.title)
                Surface(
                    color = Color.Black
                ) {
                    Column {
                        PreviewTicketDurationView(
                            validFrom = ticket.validFrom.formatTime(),
                            validTo = ticket.validTo.formatTime()
                        )
                        PreviewTicketOwnerView(owner = ticket.owner)
                    }
                }
            }
        }
    }
}
@Composable
fun PreviewTicketTitleView(
    title: String
) {
    Row(
        modifier = Modifier.padding(Dimensions.LARGER)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.9f),
            color = Color.Black
        )
        Image(
            imageVector = Icons.AutoMirrored.Default.ArrowForward,
            contentDescription = "Arrow Right",
            modifier = Modifier.weight(0.1f)
        )
    }
}
@Composable
fun PreviewTicketDurationView(
    validFrom: String,
    validTo: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimensions.LARGER,
                vertical = Dimensions.MEDIUM
            ),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.SMALL)
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_schedule_24),
            contentDescription = "Clock",
            colorFilter = ColorFilter.tint(
                color = Color.LightGray,
            )
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.SMALL)
        ) {
            Text(
                text = "Von:",
                color = Color.LightGray
            )
            Text(
                text = "Bis:",
                color = Color.LightGray
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.SMALL)
        ) {

            Text(
                text = validFrom,
                color = Color.White
            )

            Text(
                text = validTo,
                color = Color.White
            )
        }
    }
}

@Composable
fun PreviewTicketOwnerView(
    owner: String
) {
    Row(
        modifier = Modifier
            .padding(horizontal = Dimensions.LARGER)
            .padding(bottom = Dimensions.MEDIUM),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.SMALL)
    ) {
        Image(
            imageVector = Icons.Default.Person,
            contentDescription = "Clock",
            colorFilter = ColorFilter.tint(
                color = Color.LightGray
            )
        )
        Text(
            text = owner,
            color = Color.White
        )
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun TicketListPreview() {
    STUxPassTheme {
        TicketListView(
            navigateToTicket = {}
        )
    }
}