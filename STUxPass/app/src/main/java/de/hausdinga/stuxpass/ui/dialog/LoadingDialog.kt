package de.hausdinga.stuxpass.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import de.hausdinga.stuxpass.ui.theme.STUxPassTheme
import de.hausdinga.stuxpass.util.Dimensions

@Composable
fun LoadingDialog(
    showDialog: Boolean
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { }
        ) {
            Card {
                Column(
                    modifier = Modifier
                        .padding(Dimensions.LARGE),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.MEDIUM),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Loading...")
                }
            }
        }
    }
}

@Preview
@Composable
fun LoadingDialogPreview() {
    STUxPassTheme {
        Scaffold {
            Surface(modifier = Modifier.padding(it)) {
                LoadingDialog(
                    showDialog = true
                )
            }
        }
    }
}