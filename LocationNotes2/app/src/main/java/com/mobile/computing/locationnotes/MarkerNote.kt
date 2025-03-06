package com.mobile.computing.locationnotes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarkerNote(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val lon: Double,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "note") var note: String
)

data class MarkerNoteContents(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val note: String
)