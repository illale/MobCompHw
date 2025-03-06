package com.mobile.computing.locationnotes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MarkerDao {

    @Query("SELECT * FROM markernote")
    fun getAll(): List<MarkerNote>

    @Insert(entity = MarkerNote::class)
    fun insertMarker(markerNoteContents: MarkerNoteContents)

    @Delete
    fun deleteMarker(markerNote: MarkerNote)

    @Update
    fun updateMarker(markerNote: MarkerNote)
}