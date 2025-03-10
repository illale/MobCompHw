package com.mobile.computing.locationnotes

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.util.Locale


@Composable
fun Map(
    context: Context,
    activity: MainActivity,
    paddingValues: PaddingValues,
    markerDatabase: MarkerDatabase
) {
    val uiSettings by remember { mutableStateOf(
        MapUiSettings(
            compassEnabled = true,
            indoorLevelPickerEnabled = true,
            mapToolbarEnabled = true,
            myLocationButtonEnabled = true
        )
    ) }
    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = false)) }
    var userLocation by remember {
        mutableStateOf(LatLng(10.0, 10.0))
    }
    var zoomLevel by remember {
        mutableFloatStateOf(10f)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, zoomLevel)
    }
    val showDialog = remember { mutableStateOf(false) }
    val clickPosition = remember { mutableStateOf(LatLng(0.0, 0.0)) }

    getCurrentLocation(context, activity) { location ->
        properties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true)
        userLocation = location
        zoomLevel = cameraPositionState.position.zoom
        cameraPositionState.position = CameraPosition.fromLatLngZoom(location, zoomLevel)
    }

    if (showDialog.value) {
        LocationDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            latLng = clickPosition.value,
            markerDatabase = markerDatabase,
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        properties = properties,
        uiSettings = uiSettings,
        cameraPositionState = cameraPositionState,
        onMapLongClick = { latLng ->
            clickPosition.value = latLng
            showDialog.value = true
        },
        onMyLocationClick = {
            getCurrentLocation(context, activity) { latLng ->
                zoomLevel = cameraPositionState.position.zoom
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, zoomLevel)
            }
        }
    ) {
        val markerNotes = markerDatabase.markerDao().getAll()
        for (note in markerNotes) {
            MapMarker(note)
        }
    }
}

fun getCurrentLocation(context: Context, mainActivity: MainActivity, onLocationUpdate: (LatLng) -> Unit) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            mainActivity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ), 3
        )
    }
    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onLocationUpdate(LatLng(location.latitude, location.longitude))
        }
    }
}

@Composable
fun MapMarker(markerNote: MarkerNote) {
    val markerState = rememberMarkerState(
        position = LatLng(markerNote.lat, markerNote.lon)
    )
    MarkerInfoWindowContent(
        state = markerState,
        title = markerNote.name,
        onClick = {
            markerState.showInfoWindow()
            return@MarkerInfoWindowContent true
        },
        content = {
            NoteCard(markerNote)
        }
    )
}

@Composable
fun NoteCard(markerNote: MarkerNote) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth(0.4F)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = "%.2f° N, %.2f° W".format(Locale.US, markerNote.lat, markerNote.lon),
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Start
            )

            Text(
                text = markerNote.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = markerNote.note,
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
fun LocationDialog(
    onDismissRequest: () -> Unit,
    latLng: LatLng,
    markerDatabase: MarkerDatabase
) {
    var markerName by remember { mutableStateOf("") }
    var markerNote by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    markerDatabase.markerDao().insertMarker(MarkerNoteContents(
                        latitude = latLng.latitude,
                        longitude = latLng.longitude,
                        name = markerName,
                        note = markerNote
                    ))
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
            Text("Enter a new note")
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