package com.mobile.computing.hw3

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun MessageCard(msg: Message) {

    Row(modifier = Modifier.padding(all = 8.dp)) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(msg.path)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.tintin),
            contentDescription = "Profile image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape).size(40.dp).border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = "Surface color"
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.large,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.content,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}

@Composable
fun Conversation(messages: List<Message>, onNavigateToProfile: () -> Unit, onNavigateToWriter: () -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(messages) { message ->
            MessageCard(message)
        }
        item() {
            Button(onClick = onNavigateToProfile) {
                Text(text = "Profile")
            }
        }
        item() {
            Button(onClick = onNavigateToWriter) {
                Text(text = "Writer")
            }
        }
    }
}