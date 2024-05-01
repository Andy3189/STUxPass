package de.hausdinga.stuxpass.ui.pass

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import de.hausdinga.stuxpass.local.database.Ticket
import de.hausdinga.stuxpass.ui.theme.STUxPassTheme
import de.hausdinga.stuxpass.util.Dimensions
import de.hausdinga.stuxpass.viewmodel.SharedViewModel
import de.hausdinga.stuxpass.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pass(
    ticketID: String,
    viewModel: SharedViewModel = hiltViewModel(),
    navigateBack: () -> Unit) {
    val ticket by viewModel.ticketUiState
    LaunchedEffect(true) {
        viewModel.selectTicket(ticketID)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text ("Ticket Details")},
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Image(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back Button")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    ) { paddingValues ->
        PassContentView(
            state = ticket,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Pass(state: UiState) {
    val ticket by remember { mutableStateOf(state) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text ("Ticket Details")},
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Image(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back Button")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    ) { paddingValues ->
        PassContentView(
            state = ticket,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun PassContentView(
    state: UiState,
    modifier: Modifier = Modifier
) {
    when (state) {
        is UiState.Loading -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(Dimensions.LARGER),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Loading...")
            }
        }
        is UiState.Success<*> -> {
            BVGPassView(
                ticket = state.data as Ticket,
                modifier = modifier
            )
        }
        is UiState.Idle -> {
            Text(
                text = "TODO implement Idle",
                modifier = modifier
            )

        }
        else -> {
            Text(
                text = "TODO implement Error",
                modifier = modifier
            )
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PassPreview() {
    STUxPassTheme {
        Pass(
            state = UiState.Loading
        )
    }
}