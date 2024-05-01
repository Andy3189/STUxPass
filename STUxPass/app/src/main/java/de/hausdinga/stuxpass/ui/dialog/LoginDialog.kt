package de.hausdinga.stuxpass.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import de.hausdinga.stuxpass.ui.theme.STUxPassTheme
import de.hausdinga.stuxpass.util.Dimensions
import de.hausdinga.stuxpass.util.formatDate
import de.hausdinga.stuxpass.util.toLocalDate
import java.time.LocalDate

@Composable
fun LoginDialog(
    showDialog: Boolean,
    onSubmit: (date: LocalDate, immaNumber:String) -> Unit,
    onDismiss: () -> Unit
) {
    var immaNumber by remember {
        mutableStateOf("")
    }
    var birthdate: LocalDate? by remember {
        mutableStateOf(null)
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    val outsideController = LocalSoftwareKeyboardController.current
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                outsideController?.hide()
                showDatePicker = false
                onDismiss()
            }
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            Card {
                Column(
                    modifier = Modifier
                        .padding(Dimensions.LARGE),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.MEDIUM)
                ) {
                    Text(
                        text = "Enter Credentials",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(Dimensions.SMALL),
                        fontWeight = FontWeight.Medium
                    )
                    DatePickerTextField(
                        value = TextFieldValue(birthdate?.formatDate() ?: ""),
                        onClick = {
                            showDatePicker = true
                        }) {
                        Text("Birthdate")
                    }
                    TextField(
                        value = immaNumber,
                        onValueChange = {
                            immaNumber = it
                        },
                        label = { Text("Matriculation Number")},
                        leadingIcon = {
                              Icon(
                                  imageVector = Icons.Default.AccountBox,
                                  contentDescription = "Card Icon"
                              )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.padding(Dimensions.MEDIUM),
                        ) {
                            Text("Dismiss")
                        }
                        TextButton(
                            onClick = {
                                birthdate?.let {
                                    onSubmit(it, immaNumber)
                                }
                            },
                            modifier = Modifier.padding(Dimensions.MEDIUM),
                            enabled = immaNumber.isNotEmpty() && birthdate != null
                        ) {
                            Text("Confirm")
                        }
                    }

                }
            }
        }
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { selectedDate ->
                birthdate = selectedDate
            }
        )
    }
}

@Composable
fun DatePickerTextField(
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit) {
    Box {
        TextField(
            value = value,
            onValueChange = { },
            modifier = modifier,
            label = label,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Icon")
            }
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.toLocalDate()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    selectedDate?.let { onDateSelected(it) }
                    onDismiss()
                }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Preview
@Composable
fun LoginDialogDisplayPreview() {
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    STUxPassTheme {
        Scaffold {
            Surface(modifier = Modifier.padding(it)) {
                Button(onClick = { showDatePicker = true }) {
                    Text("Display Dialog")
                }
                LoginDialog(
                    showDialog = showDatePicker,
                    onSubmit = {_,_ -> },
                    onDismiss = { showDatePicker = false }
                )
            }
        }
    }
}
@Preview
@Composable
fun LoginDialogPreview() {
    var showDatePicker by remember {
        mutableStateOf(true)
    }
    STUxPassTheme {
        Scaffold {
            Surface(modifier = Modifier.padding(it)) {
                LoginDialog(
                    showDialog = showDatePicker,
                    onSubmit = {_,_ -> },
                    onDismiss = { showDatePicker = false }
                )
            }
        }
    }
}