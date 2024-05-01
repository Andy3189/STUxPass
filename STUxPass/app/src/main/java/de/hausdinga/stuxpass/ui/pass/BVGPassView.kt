package de.hausdinga.stuxpass.ui.pass

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import de.hausdinga.stuxpass.R
import de.hausdinga.stuxpass.local.database.Ticket
import de.hausdinga.stuxpass.ui.aztec.AztecView
import de.hausdinga.stuxpass.ui.theme.FADED_GRAY
import de.hausdinga.stuxpass.ui.theme.FADED_WHITE
import de.hausdinga.stuxpass.ui.theme.RAL1023
import de.hausdinga.stuxpass.ui.theme.STUxPassTheme
import de.hausdinga.stuxpass.ui.theme.TextRow
import de.hausdinga.stuxpass.util.Dimensions
import de.hausdinga.stuxpass.util.formatTime
import kotlinx.coroutines.Dispatchers

@Composable
fun BVGPassView(
    ticket: Ticket,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize(), color = RAL1023) {
        CardView(ticket = ticket)
    }
}

@Composable
fun CardView(
    ticket: Ticket
) {
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .padding(Dimensions.LARGE),
        color = FADED_WHITE,
        shape = MaterialTheme.shapes.large,
    ) {
        Column {
            CardHeaderView(ticket = ticket)
            CardBodyView(ticket = ticket)
        }
    }
}

@Composable
fun CardHeaderView(
    ticket: Ticket
) {
    Column {
        Spacer(modifier = Modifier.height(Dimensions.LARGE))
        Text(
            text = ticket.title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = Black
        )
        Spacer(modifier = Modifier.height(Dimensions.LARGE))
    }
}

@Composable
fun CardBodyView(
    ticket: Ticket
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White
    ) {
        Column {
            CardBodyIcons(ticket.iconURLs)
            AztecView(
                data = ticket.data,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimensions.LARGERER,
                        vertical = Dimensions.SMALL
                    )
            )
            CardBodyNumber(codeWord = ticket.codeWord)
            CardBodyFooterView(ticket)
        }
    }
}

@Composable
fun CardBodyIcons(
    iconURLs: List<String>,
    placeholder: Int = R.drawable.baseline_directions_bus_24
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.MEDIUM),
        horizontalArrangement = Arrangement.spacedBy(
            Dimensions.LARGE,
            Alignment.CenterHorizontally
        )
    ) {
        items(iconURLs) {
            val imageUrl = it.trim() // ride links may have stupid spaces
            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .dispatcher(Dispatchers.IO)
                .memoryCacheKey(imageUrl)
                .diskCacheKey(imageUrl)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
            AsyncImage(
                model = imageRequest,
                contentDescription = imageUrl,
                modifier = Modifier.height(Dimensions.ICON_PRESET_HEIGHT)
            )
        }
    }
}

@Composable
fun CardBodyNumber(
    codeWord: String
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimensions.LARGERER,
                vertical = Dimensions.MEDIUM
            ),
        color = White,
        border = BorderStroke(
            width = 1.dp,
            color = FADED_GRAY)
    ) {
        Column {
            Text(
                text = codeWord,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimensions.MEDIUM,
                        vertical = Dimensions.SMALLER
                    ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = Black
            )
            SlowAnimationIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimensions.LARGER,
                        vertical = Dimensions.SMALL
                    )
                    .height(Dimensions.MEDIUM),
                color = RAL1023,
                trackColor = FADED_GRAY
            )
        }
    }
}

@Composable
fun SlowAnimationIndicator(
    modifier: Modifier,
    color: Color,
    trackColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimensions.LARGE))
            .background(trackColor)
            .height(Dimensions.MEDIUM)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .background(trackColor)
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimensions.LARGE))
                    .background(color)
                    .fillMaxHeight()
                    .width(Dimensions.LARGERER)
            )
        }
    }
}
@Composable
fun CardBodyFooterView(
    ticket: Ticket
) {
    Surface(
        color = FADED_GRAY
    ) {
        Column {
            Spacer(modifier = Modifier.height(1.dp))
            HorizontalDivider(thickness = 0.75f.dp)
            CardValidInfoView(ticket = ticket)
            HorizontalDivider(thickness = 0.75f.dp)
            CardOwnerView(ticket = ticket)
            HorizontalDivider(thickness = 0.75f.dp)
            CardInfoView(ticket = ticket)
        }
    }
}

@Composable
fun CardValidInfoView(
    ticket: Ticket
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MEDIUM)
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_schedule_24),
            contentDescription = "Clock"
        )
        Spacer(modifier = Modifier.width(Dimensions.MEDIUM))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimensions.SMALL)
        ) {
            TextRow(
                title = "Gültig von:",
                message = ticket.validFrom.formatTime()
            )
            TextRow(
                title = "Gültig bis:",
                message = ticket.validTo.formatTime()
            )
            TextRow(
                title = "Geltungsbereich:",
                message = ticket.validWhere
            )
        }
    }
}

@Composable
fun CardOwnerView(
    ticket: Ticket
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.MEDIUM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = Icons.Default.Person,
            contentDescription = "Clock"
        )
        Spacer(modifier = Modifier.width(Dimensions.MEDIUM))
        Column(Modifier.weight(1f)) {
            Text(
                text = "Ticketinhaber*in:",
                style = MaterialTheme.typography.bodyLarge,
                color = Black
            )
            Text(
                text = ticket.owner,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Black
            )
        }
        Image(
            painter = painterResource(id = R.drawable.outline_receipt_24),
            contentDescription = "Clock"
        )
        Spacer(modifier = Modifier.width(Dimensions.MEDIUM))
        Column {
            Text(
                text = "Abo-Nr.:",
                style = MaterialTheme.typography.bodyLarge,
                color = Black
            )
            Text(
                text = ticket.aboNr,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Black
            )
        }
    }
}

@Composable
fun CardInfoView(
    ticket: Ticket
) {
    Text(
        text = ticket.infoText,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.LARGE),
        textAlign = TextAlign.Left,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.secondary
    )
}