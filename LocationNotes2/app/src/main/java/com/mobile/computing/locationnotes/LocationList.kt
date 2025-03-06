package com.mobile.computing.locationnotes

import android.util.Log
import androidx.collection.mutableObjectListOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun LocationList(
    paddingValues: PaddingValues,
    markerDatabase: MarkerDatabase
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val notes = markerDatabase.markerDao().getAll()
        val notesList = remember { notes.toMutableStateList() }
        Text("Location Notes: ")
        LazyColumn (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(notesList) { note ->
                LocationCard(
                    note,
                    markerDatabase,
                    {

                    },
                    {
                        Log.d("DELETE", "DELETE THIS NOTE")
                        notesList.remove(note)
                        markerDatabase.markerDao().deleteMarker(note)
                    })
            }
        }
    }
}



@Composable
fun LocationCard(
    note: MarkerNote,
    markerDatabase: MarkerDatabase,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column {
        Card {
            var editPressed by remember { mutableStateOf(false) }
            ListItem(
                modifier = Modifier.padding(4.dp),
                headlineContent = {
                    Text(text = note.name)
                },
                overlineContent = {
                    Text(text = "%.2f° N, %.2f° W".format(Locale.US, note.lat, note.lon))
                },
                supportingContent = {
                    Text(text = note.note, minLines = 3, maxLines = 100)
                },
                trailingContent = {
                    Row {
                        IconButton(onClick = {
                            editPressed = true
                        }) {
                            Icon(Icons.Default.Edit, "Edit")
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, "Delete")
                        }
                    }
                }
            )
            if (editPressed) {
                EditDialog(note, {
                    editPressed = false
                }, markerDatabase)
            }
        }
    }
}

@Composable
fun EditDialog(
    note: MarkerNote,
    onDismissRequest: () -> Unit,
    markerDatabase: MarkerDatabase
) {
    var markerName by remember { mutableStateOf(note.name) }
    var markerNote by remember { mutableStateOf(note.note) }
    AlertDialog(
        onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    note.name = markerName
                    note.note = markerNote
                    markerDatabase.markerDao().updateMarker(note)
                    onDismissRequest()
                }
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text("Edit note")
        },
        text = {
            Column {
                OutlinedTextField(
                    markerName,
                    modifier = Modifier.padding(8.dp),
                    onValueChange = { value ->
                        markerName = value
                    },
                    label = {
                        Text(text = "Name")
                    },
                    maxLines = 1,
                    singleLine = true
                )
                OutlinedTextField(
                    markerNote,
                    modifier = Modifier.padding(8.dp),
                    onValueChange = { value ->
                        markerNote = value
                    },
                    label = {
                        Text(text = "Note")
                    },
                    minLines = 5,
                    maxLines = 100,
                    singleLine = false
                )
            }
        }
    )
}