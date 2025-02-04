package com.mobile.computing.hw3

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.io.File

@Composable
fun WriterScreen(onNavigateToConservation: () -> Unit) {
    Column(
        modifier = Modifier.padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current
        val resolver = LocalContext.current.contentResolver
        var path by remember { mutableStateOf("") }
        val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            imageUri = uri
            if (uri != null) {
                resolver.openInputStream(uri).use { stream ->
                    val file = uri.lastPathSegment?.let { File(context.filesDir, it) }

                    if (file != null) {
                        if (!file.exists()) {
                            file.createNewFile()
                            file.let { stream?.copyTo(it.outputStream()) }
                            path = file.path
                        }
                    }

                }
            }
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = "Profile image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape).size(150.dp)
        )

        Button(
            onClick = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        ) {
            Text(text = "Pick image")
        }

        Spacer(modifier = Modifier.height(8.dp))

        var author by remember {
            mutableStateOf("")
        }

        TextField(
            value = author,
            onValueChange = { author = it },
            label = {
                Text("Name")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        var text by remember {
            mutableStateOf("")
        }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Text("Input")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val messageContents = MessageContents(author, text, path)
            val messageDatabase = MessageDatabase.getDatabase(context)
            messageDatabase.messageDao().insertMessage(messageContents)
            text = ""
            author = ""
            imageUri = null

        }) {
            Text(text = "Save")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToConservation) {
            Text(text = "Conservation")
        }
    }
}